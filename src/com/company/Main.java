package com.company;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler<KeyEvent> {
    public static int ALTO_VERTICAL = 30; // Margen vertical para el menú
    public static int ANCHO_PANTALLA = 800;
    public static int ALTO_PANTALLA = 600;
    public static int ANCHO = 20; // Ancho de la serpiente
    public static int VELOCIDAD_RETARDO = 100000000; //  velocidad del juego

    public enum Direccion {
        Arriba, Abajo, Izquierda, Derecha
    };

    private Direccion dir = Direccion.Arriba; // Dirección de la serpiente
    private double cabezaX = ANCHO_PANTALLA / 2;
    private double cabezaY = ALTO_PANTALLA * 4 / 5; // Poisición inicial
    private int tamaño = 1; // Tamaño inicial
    private Group raiz;
    private ArrayList<Rectangle> trozos = new ArrayList<>();// Lista de trozos
    private Circle fruta;
    private Text puntos;
    private AnimationTimer animationTimer;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage ventana) throws Exception {
        ventana.setWidth(ANCHO_PANTALLA);
        ventana.setHeight(ALTO_PANTALLA);
        ventana.setTitle("JUEGO SNAKE");
        ventana.setResizable(false);
        raiz = new Group();
        Scene escena = new Scene(raiz, Color.GREEN);
        ventana.setScene(escena);
        ponerFruta();
        puntos = new Text();
        puntos.setFont(Font.font(20));
        puntos.setFill(Color.WHITE);
        puntos.setX(10);
        puntos.setY(20);
        raiz.getChildren().add(puntos);
        ponerPuntos();

        escena.setOnKeyPressed(this);

        animationTimer = new AnimationTimer() {
            long ultimaanimacion = 0;

            @Override
            public void handle(long now) {
                if (ultimaanimacion == 0) {
                    ultimaanimacion = now;
                    return;
                }
                if (now - ultimaanimacion < VELOCIDAD_RETARDO)
                    return;

                switch (dir) {
                    case Abajo:
                        cabezaY += ANCHO;
                        break;
                    case Arriba:
                        cabezaY -= ANCHO;
                        break;
                    case Derecha:
                        cabezaX += ANCHO;
                        break;
                    case Izquierda:
                        cabezaX -= ANCHO;
                        break;
                }
                // Al salir de la pantaolla
                if (cabezaX < 0)
                    cabezaX = ANCHO_PANTALLA;
                if (cabezaX > ANCHO_PANTALLA)
                    cabezaX = 0;
                if (cabezaY < 0)
                    cabezaY = ALTO_PANTALLA - ALTO_VERTICAL;
                if (cabezaY > ALTO_PANTALLA - ALTO_VERTICAL)
                    cabezaY = 0;

                // Al chocar
                for (Rectangle r : trozos)
                    if (r.getX() == cabezaX)
                        if (r.getY() == cabezaY)
                            gameOver();

                // Comer
                if (fruta.getCenterX() >= cabezaX && fruta.getCenterX() <= cabezaX + ANCHO)
                    if (fruta.getCenterY() >= cabezaY && fruta.getCenterY() <= cabezaY + ANCHO) {
                        System.out.println("Come fruta");
                        tamaño++;
                        raiz.getChildren().remove(fruta);
                        ponerFruta();
                        ponerPuntos();
                    }
                // Mover  la serpiente
                Rectangle trozo = new Rectangle(ANCHO, ANCHO);
                trozo.setFill(Color.BLUE);
                trozo.setStroke(Color.BLACK);
                trozo.setX(cabezaX);
                trozo.setY(cabezaY);
                raiz.getChildren().add(trozo);
                if (!trozos.isEmpty()) {
                    if (trozos.size() >= tamaño)
                        //Quitamos el último trozo
                        raiz.getChildren().remove(trozos.remove(0));
                }
                trozos.add(trozo);	//Ponemos un nuevo trozo
                ultimaanimacion = now;
            }
        };

        ventana.show();
        animationTimer.start();
    }

    private void ponerPuntos() {
        puntos.setText("Pts: " + (tamaño - 1));
    }

    private void ponerFruta() {
        fruta = new Circle(ANCHO / 2 - 1);
        fruta.setFill(Color.RED);
        double x = Math.rint(Math.random() * (ANCHO_PANTALLA - ANCHO) / ANCHO) * ANCHO + ANCHO / 2;
        double y = Math.rint(Math.random() * (ALTO_PANTALLA - ALTO_VERTICAL - ANCHO) / ANCHO) * ANCHO + ANCHO / 2;

        fruta.setCenterX(x);
        fruta.setCenterY(y);
        raiz.getChildren().add(fruta);
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                if (!dir.equals(Direccion.Abajo))
                    dir = Direccion.Arriba;
                break;
            case DOWN:
                if (!dir.equals(Direccion.Arriba))
                    dir = Direccion.Abajo;
                break;
            case RIGHT:
                if (!dir.equals(Direccion.Izquierda))
                    dir = Direccion.Derecha;
                break;
            case LEFT:
                if (!dir.equals(Direccion.Derecha))
                    dir = Direccion.Izquierda;
                break;
            default:
        }
        // System.out.println(this.dir);
    }

    private void gameOver() {
        animationTimer.stop();
        Text gameOver = new Text("GAME OVER LOSER");
        gameOver.setFont(Font.font(45));
        gameOver.setFill(Color.DARKRED);
        gameOver.setStroke(Color.WHITE);
        gameOver.setY(ALTO_PANTALLA / 2);
        gameOver.setX(ANCHO_PANTALLA / 2 - 125);
        raiz.getChildren().add(gameOver);
    }

}