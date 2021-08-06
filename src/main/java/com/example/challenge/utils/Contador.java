package com.example.challenge.utils;

import com.example.challenge.domain.services.PautaService;

import java.util.Timer;
import java.util.TimerTask;

public class Contador {
    Timer timer;
    PautaService service;

    public Contador(int minutos, Long pautaId, PautaService pautaService) {
        service = pautaService;
        timer = new Timer();
        timer.schedule(new EncerraVotacao(pautaId), minutos * 60000);
    }

    public class EncerraVotacao extends TimerTask {
        private Long pautaId;

        public EncerraVotacao(Long id) {
            this.pautaId = id;
        }

        public void run() {
            service.encerraVotacao(pautaId);
            timer.cancel();
        }
    }
}
