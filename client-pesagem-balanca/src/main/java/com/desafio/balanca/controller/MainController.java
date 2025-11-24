package com.desafio.balanca.controller;


import com.desafio.balanca.service.BalancaClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import com.desafio.balanca.model.SimuladorBalanca;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainController {
    
    @FXML private TextField scaleIdField;
    @FXML private TextField plateField;
    @FXML private TextField targetWeightField;
    @FXML private TextField serverUrlField;
    
    @FXML private Text currentWeightText;
    @FXML private Text statusText;
    @FXML private ProgressIndicator progressIndicator;
    
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private Button resetButton;
    @FXML private Button retryFailedButton;
    
    @FXML private TextArea logArea;
    @FXML private Label failedRecordsLabel;
    
    private SimuladorBalanca simulator;
    private BalancaClient client;
    private ScheduledExecutorService scheduler;
    private boolean isRunning = false;
    
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    
    @FXML
    public void initialize() {
        // Set default values
        scaleIdField.setText("BAL001");
        serverUrlField.setText("http://localhost:8080");
        targetWeightField.setText("15000");
        
        stopButton.setDisable(true);
        progressIndicator.setVisible(false);
        
        log("Sistema iniciado. Aguardando dados...");
    }
    
    @FXML
    private void handleStart() {
        if (isRunning) return;
        
        String scaleId = scaleIdField.getText().trim();
        String plate = plateField.getText().trim();
        String targetWeightStr = targetWeightField.getText().trim();
        String serverUrl = serverUrlField.getText().trim();
        
        if (scaleId.isEmpty() || plate.isEmpty() || targetWeightStr.isEmpty()) {
            showError("Preencha todos os campos obrigatórios!");
            return;
        }
        
        try {
            BigDecimal targetWeight = new BigDecimal(targetWeightStr);
            
            simulator = new SimuladorBalanca(scaleId, targetWeight);
            client = new BalancaClient(serverUrl);
            
            isRunning = true;
            startButton.setDisable(true);
            stopButton.setDisable(false);
            resetButton.setDisable(true);
            progressIndicator.setVisible(true);
            
            log("Iniciando simulação de pesagem...");
            log("Balança: " + scaleId + " | Placa: " + plate + " | Peso alvo: " + targetWeight + " kg");
            
            startSimulation(plate);
            
        } catch (NumberFormatException e) {
            showError("Peso inválido! Use apenas números.");
        }
    }
    
    private void startSimulation(String plate) {
        scheduler = Executors.newScheduledThreadPool(1);
        
        scheduler.scheduleAtFixedRate(() -> {
            if (!isRunning) return;
            
            SimuladorBalanca.WeightReading reading = simulator.simulateNextReading();
            
            Platform.runLater(() -> {
                currentWeightText.setText(String.format("%.2f kg", reading.getWeight()));
                
                if (reading.isStabilized()) {
                    statusText.setText("✓ PESO ESTABILIZADO");
                    statusText.setFill(Color.GREEN);
                    
                    // Send to server
                    sendWeightToServer(reading.getScaleId(), plate, reading.getWeight());
                    
                    // Stop simulation
                    stopSimulation();
                } else {
                    statusText.setText("⚖ ESTABILIZANDO...");
                    statusText.setFill(Color.ORANGE);
                    log(String.format("Peso atual: %.2f kg", reading.getWeight()));
                }
            });
            
        }, 0, 500, TimeUnit.MILLISECONDS);
    }
    
    private void sendWeightToServer(String scaleId, String plate, BigDecimal weight) {
        new Thread(() -> {
            log("Enviando dados ao servidor...");
            BalancaClient.SendResult result = client.sendWeighingRecord(scaleId, plate, weight);
            
            Platform.runLater(() -> {
                if (result.isSuccess()) {
                    log("Dados enviados com sucesso!");
                    statusText.setText("ENVIADO AO SERVIDOR");
                    statusText.setFill(Color.GREEN);
                } else {
                    log("Falha ao enviar: " + result.getMessage());
                    
                    statusText.setText("ERRO");
                    statusText.setFill(Color.RED);
                }
            });
        }).start();
    }
    
    @FXML
    private void handleStop() {
        stopSimulation();
    }
    
    private void stopSimulation() {
        isRunning = false;
        
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        
        Platform.runLater(() -> {
            startButton.setDisable(false);
            stopButton.setDisable(true);
            resetButton.setDisable(false);
            progressIndicator.setVisible(false);
            log("Simulação parada.");
        });
    }
    
    @FXML
    private void handleReset() {
        currentWeightText.setText("0.00 kg");
        statusText.setText("AGUARDANDO");
        statusText.setFill(Color.GRAY);
        plateField.clear();
        log("Sistema resetado.");
    }
    
    @FXML
    private void handleClearLog() {
        logArea.clear();
    }
    
    private void log(String message) {
        String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
        Platform.runLater(() -> {
            logArea.appendText("[" + timestamp + "] " + message + "\n");
        });
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
