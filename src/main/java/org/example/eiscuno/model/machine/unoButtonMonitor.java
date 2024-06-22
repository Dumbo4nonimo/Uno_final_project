package org.example.eiscuno.model.machine;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.player.Player;

public class unoButtonMonitor extends Thread {
    private final Player machinePlayer;
    private GameUnoController gameUnoController;
    private final int TIMEOUT = 2000; // 2 segundos

    public unoButtonMonitor(Player machinePlayer, GameUnoController gameUnoController) {
        this.machinePlayer = machinePlayer;
        this.gameUnoController = gameUnoController;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Verificar si la máquina tiene solo una carta
                if (machinePlayer.getCardsPlayer().size() == 1) {
                    long startTime = System.currentTimeMillis();
                    boolean buttonPressed = false;
                    boolean timeoutReached = false;

                    while (System.currentTimeMillis() - startTime < TIMEOUT) {
                        if (gameUnoController.getUnoButtonPressed()) {
                            buttonPressed = true;
                            break;
                        }
                        Thread.sleep(100); // Dormir por un corto tiempo antes de verificar nuevamente
                    }

                    if (buttonPressed) {
                        gameUnoController.eatMachineCards();
                    } else {
                        timeoutReached = true;
                    }

                    // Resetear el estado del botón después de la verificación
                    gameUnoController.restarButton();

                    if (timeoutReached) {
                        // Dormir por un corto tiempo para evitar múltiples ejecuciones para la misma carta
                        Thread.sleep(1000);
                    }
                } else {
                    // Dormir durante un tiempo antes de verificar nuevamente
                    Thread.sleep(500); // Dormir durante 0.5 segundos para reducir la frecuencia de verificación
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
