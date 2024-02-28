package com.example.snake;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SnakeActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    /***********Declaración de variables***********/
    // Longitud de la serpiente
    private final List<SnakePoints> snakePointsList = new ArrayList<>();
    // Variables relacionados al layout
    private Button bt_top;
    private Button bt_bottom;
    private Button bt_left;
    private Button bt_right;
    // Pantalla por la que se moverá la serpient y puntuación
    private SurfaceView pantallaJuego;
    private TextView tv_score;
    //Permite "dibujar" la serpiente por el canvas / pantalla de juego
    private SurfaceHolder surfaceHolder;
    // Movimiento de la serpiente, serán: arriba, abajo, izquierda y derecha. Por defecto será la derecha
    private String movingPosition = "right";
    // Puntuación que se iniciará a 0
    private int score = 0;
    // Anchura de la serpiente. Se puede variar este valor para cambiar el tamaño de la serpiente
    private static final int pointSize = 25;
    // Longitud de la serpiente. Por defecto será de 3
    private static final int defaultTalePoints = 3;
    // color de la serpiente y la comida
    private static final int snakeColor = Color.GREEN;
    private static final int foodColor = Color.RED;
    // velocidad del movimiento de la serpiente debe de ser entre 1 y 1000
    private static final int snakeMovingSpeed = 500;
    private int snakeSpeed = snakeMovingSpeed;  /* Variable para modificar la velocidad una vez iniciada la aplicación */
    // Coordenadas para la comida y serpiente en pantallaJuego
    private int positionX, positionY;
    // timer sirve para mover las posiciones de la serpiente despues de un tiempo específico (snakeMovingSpeed)
    private Timer timer;
    //Canvas para dibujar en la pantalla de juego
    private Canvas canvas;
    // Puntos de dibujado de serpiente y comida
    private Paint snakePointColor = null;
    private Paint foodPointColor = null;

    /*********** Creación app ***********/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);

        //Asociacion de botones con la id
        pantallaJuego = findViewById(R.id.pantallaJuego);
        tv_score = findViewById(R.id.tv_puntuacion);

        final AppCompatImageButton bt_top = findViewById(R.id.topBtn);
        final AppCompatImageButton bt_left = findViewById(R.id.leftBtn);
        final AppCompatImageButton bt_right = findViewById(R.id.rightBtn);
        final AppCompatImageButton bt_bottom = findViewById(R.id.bottomBtn);

        // Hace que los botones solo se activen una vez alla ocurrido un cambio en la pantalla
        pantallaJuego.getHolder().addCallback(this);
        /****Listeners****/
        bt_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprueba que el movimiento al inicio no se hacia abajo. En ese caso lo cambiaremos a izq, drch o arriba
                if(!movingPosition.equals("bottom")){
                    movingPosition = "top";
                }
            }
        });

        bt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!movingPosition.equals("right")){
                    movingPosition = "left";
                }
            }
        });

        bt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!movingPosition.equals("left")){
                    movingPosition = "right";
                }
            }
        });

        bt_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!movingPosition.equals("top")){
                    movingPosition = "bottom";
                }
            }
        });
    }

    /***********Métodos***********/
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        init();
    }
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    /* Método para iniciar el juego y reseteo */
    private void init(){
        // Deja a la serpiente sin nada
        snakePointsList.clear();

        // Puntuación en pantalla será de 0
        tv_score.setText("0");

        // Puntuación del juego de 0
        score = 0;

        // Posición inicial hacia la derecha
        movingPosition="right";

        // Posición por defecto de inicio de la serpiente
        int startPositionX = (pointSize) * defaultTalePoints;

        // Crear la longitud por defecto de la serpiente
        for (int i = 0; i < defaultTalePoints; i++){
            // Añade los puntos al final de la serpiente
            SnakePoints snakePoints = new SnakePoints(startPositionX, pointSize);
            snakePointsList.add(snakePoints);

            // Aumenta la posición del cuerpo de la serpiente para crear más serpiente
            startPositionX = startPositionX - (pointSize * 2);
        }
        // Comida en puntos aleatorios
        addPoint();
        // Movimiento de la serpiente / Empieza el juego
        moveSnake();
    }

    private void addPoint(){
        // Coge el tamaño de pantallaJuego (width, height) para añadir un punto de comida en la pantalla
        int surfaceWidth = pantallaJuego.getWidth() - (pointSize * 2);
        int surfaceHeight = pantallaJuego.getHeight() - (pointSize * 2);

        // Creamos un punto aleatorio en la pantalla que no sobrepase los límites de la pantalla
        int randomXPosition = new Random().nextInt(surfaceWidth / pointSize);
        int randomYPosition = new Random().nextInt(surfaceHeight / pointSize);

    /* Debido al movimiento de la serpiente, las posiciones de la comida solo pueden ser pares */
        // Comprueba si randomXPosition es par o impar. En caso de que sea impar se añade uno
        if ((randomXPosition % 2) != 0){
            randomXPosition = randomXPosition + 1;
        }

        // Comprueba si randomYPosition es par o impar. En caso de que sea impar se añade uno
        if ((randomYPosition % 2) != 0){
            randomYPosition = randomYPosition + 1;
        }

        positionX = (pointSize * randomXPosition) + pointSize;
        positionY = (pointSize * randomYPosition) + pointSize;
    }

    private void moveSnake(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Coge la posición de la cabeza de la serpiente para posicionarla donde debe
                int headPositionX = snakePointsList.get(0).getPositionX();
                int headPositionY = snakePointsList.get(0).getPositionY();

                // Comprueba si ha comido la comida
                if (headPositionX == positionX && positionY == headPositionY){
                /* En caso de que si haya comido */
                    // Hacemos crecer la serpiente
                    growSnake();
                    // Se crea un nuevo punto aleatorio de comida
                    addPoint();
                }

                // Comprobamos la dirección de la serpiente
                switch (movingPosition){
                    case "right":
                        // Mover la serpiente hacia la derecha
                        // El resto de puntos seguirá el movimiento de la cabeza
                        snakePointsList.get(0).setPositionX(headPositionX + (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headPositionY);
                        break;
                    case "left":
                        // Mover la serpiente hacia la izquierda
                        snakePointsList.get(0).setPositionX(headPositionX - (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headPositionY);
                        break;
                    case "top":
                        // Mover la serpiente hacia arriba
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY - (pointSize * 2));
                        break;
                    case "bottom":
                        // Mover la serpiente hacia abajo
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY + (pointSize * 2));
                        break;
                }

                // Comprobar si la serpiente se ha chocado con sigo misma o la pared para terminar el juego
                if (checkGameOver(headPositionX,headPositionY)){
                    // Parar timer
                    timer.purge();
                    timer.cancel();
                    // Alerta con la puntuación y un aviso de Game Over
                    AlertDialog.Builder builder = new AlertDialog.Builder(SnakeActivity.this);
                    builder.setMessage("Tu puntuación fue de: " + score);
                    builder.setTitle("Game Over");
                    // Sirve para decir si se puede quitar la alerta o no
                    builder.setCancelable(false);
                    // También incluimos botones
                    // Uno para comenzar de nuevo una partida / Positivo
                    // Otro para finalizar el juego y llevar al jugador al menu principal / Negativo
                    builder.setPositiveButton("Volver a empezar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Reiniciar juego
                            init();
                        }
                    });
                    builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Terminar el juego
                            finish();
                        }
                    });

                    // Hace que corra en la parte de atrás de la aplicación
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    });
                } else {
                    // Mantiene canvas en surfaceHolder para poder dibujar la serpiente
                    canvas = surfaceHolder.lockCanvas();

                    // Reinicia el canvas dajándolo blanco.
                    // PorterDuff.Mode.CLEAR hace que no se vea el blanco cada vez que hay un cambio
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);

                    // Cambia la posición de la cabeza de la serpiente y del resto del cuerpo
                    canvas.drawCircle(snakePointsList.get(0).getPositionX(), snakePointsList.get(0).getPositionY(), pointSize, createPointColor());

                    // Dibuja un punto aleatorio de comida
                    canvas.drawCircle(positionX, positionY, pointSize, createRandomFood());

                    // Dibujar cabeza y cuerpo de la serpiente cada cierto tiempo
                    for (int i = 1; i < snakePointsList.size(); i++){
                        int getTempPositionX = snakePointsList.get(i).getPositionX();
                        int getTempPositionY = snakePointsList.get(i).getPositionY();

                        // Mueve los puntos del cuerpo en su posición correspondiente
                        snakePointsList.get(i).setPositionX(headPositionX);
                        snakePointsList.get(i).setPositionY(headPositionY);
                        canvas.drawCircle(snakePointsList.get(i).getPositionX(), snakePointsList.get(i).getPositionY(), pointSize, createPointColor());

                        // Cambia la posición de la cabeza
                        headPositionX = getTempPositionX;
                        headPositionY = getTempPositionY;
                    }
                    // Permite al canvas dibujar sobre pantallaJuego
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }, 1000 - snakeSpeed, 1000 - snakeSpeed);
    }

    private void growSnake(){
        // Crea un nuevo punto / parte a la serpiente
        SnakePoints snakePoints = new SnakePoints(0,0);
        // Añade la nueva parte a la serpiente justo al final de esta
        snakePointsList.add(snakePoints);

        // Aumenta la puntuación
        score++;

        // Escribe la puntuación en el TextView
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_score.setText(String.valueOf(score));
            }
        });
    }

    private boolean checkGameOver(int headPositionX, int headPositionY){
        boolean gameOver = false;
        // Comprueba si la serpiente ha tocado los bordes de la pantalla
        if (snakePointsList.get(0).getPositionX() < 0 ||
                snakePointsList.get(0).getPositionY() < 0 ||
                snakePointsList.get(0).getPositionX() >= pantallaJuego.getWidth() ||
                snakePointsList.get(0).getPositionY() >= pantallaJuego.getHeight())
        {
            gameOver = true;
        }
        else {
            // Comprueba si la serpiente ha tocado alguna parte de si misma
            for (int i = 0; i < snakePointsList.size(); i++) {
                if (headPositionX == snakePointsList.get(i).getPositionX() &&
                        headPositionY == snakePointsList.get(i).getPositionY()){
                    gameOver = true;
                    break;
                }

            }
        }
        return gameOver;
    }

    private Paint createPointColor(){
        // Comprueba si se ha pintado alguna vez la serpiente
        if (snakePointColor == null) {
            snakePointColor = new Paint();
            snakePointColor.setColor(snakeColor);
            snakePointColor.setStyle(Paint.Style.FILL);
            snakePointColor.setAntiAlias(true);// Suaviza los bordes
        }
        return snakePointColor;

    }
    private Paint createRandomFood(){
        // Comprueba si se ha pintado alguna vez la comida
        if (foodPointColor == null) {
            foodPointColor = new Paint();
            foodPointColor.setColor(foodColor);
            foodPointColor.setStyle(Paint.Style.FILL);
            foodPointColor.setAntiAlias(true);// Suaviza los bordes
        }
        return foodPointColor;

    }
}
