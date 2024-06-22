package org.example.eiscuno.model.machine;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.player.Player;

import java.util.concurrent.atomic.AtomicBoolean;

public class unoButtonMonitor extends Thread {
    private final Player machinePlayer;
    private GameUnoController gameUnoController;
    private final int TIMEOUT = 2000; // 2 segundos
    private AtomicBoolean unoButtonPressed;
    private AtomicBoolean alreadyChecked; // Nueva variable para verificar el estado

    public unoButtonMonitor(Player machinePlayer, GameUnoController gameUnoController) {
        this.machinePlayer = machinePlayer;
        this.gameUnoController = gameUnoController;
        this.unoButtonPressed = new AtomicBoolean(false);
        this.alreadyChecked = new AtomicBoolean(false); // Inicializar la nueva variable
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Verificar si la máquina tiene solo una carta
                if (machinePlayer.getCardsPlayer().size() == 1) {
                    // Verificar si ya hemos chequeado para esta carta
                    if (!alreadyChecked.get()) {
                        long startTime = System.currentTimeMillis();

                        // Resetear el estado del botón antes de verificar
                        unoButtonPressed.set(false);

                        // Esperar TIMEOUT antes de verificar
                        while (System.currentTimeMillis() - startTime < TIMEOUT) {
                            Thread.sleep(100); // Dormir por un corto tiempo antes de verificar nuevamente
                        }

                        // Verificar el estado del botón después del tiempo de espera
                        if (unoButtonPressed.get()) {
                            // Acción si no se presionó el botón dentro del tiempo
                            gameUnoController.eatMachineCards();
                        }

                        // Marcar como verificado
                        alreadyChecked.set(true);
                    }
                } else {
                    // Resetear el estado de verificación cuando el jugador no tiene una sola carta
                    alreadyChecked.set(false);
                }

                // Dormir durante un tiempo antes de verificar nuevamente
                Thread.sleep(500); // Dormir durante 0.5 segundos para reducir la frecuencia de verificación
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setUnoButtonPressed() {
        unoButtonPressed.set(true);
    }
}
