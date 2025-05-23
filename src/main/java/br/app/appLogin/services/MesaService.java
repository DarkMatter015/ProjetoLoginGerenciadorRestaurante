package br.app.appLogin.services;

import br.app.appLogin.dtos.MesaDTO;
import br.app.appLogin.exceptions.MesaException;
import br.app.appLogin.models.MesaModel;
import br.app.appLogin.repositories.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    @Transactional
    public void criarMesa(MesaDTO mesaDTO) {
        if (mesaDTO == null || mesaDTO.numero() == null || mesaDTO.capacidade() == null || mesaDTO.status() == null) {
            throw new MesaException("Dados da mesa são obrigatórios");
        }
        if (mesaRepository.existsByNumero(mesaDTO.numero())) {
            throw new MesaException("Número da mesa já existe");
        }
        MesaModel mesa = new MesaModel();
        mesa.setNumero(mesaDTO.numero());
        mesa.setCapacidade(mesaDTO.capacidade());
        mesa.setStatus(mesaDTO.status());
        mesaRepository.save(mesa);
    }

    @Transactional(readOnly = true)
    public List<MesaDTO> listarMesas() {
        return mesaRepository.findAll().stream()
                .map(m -> new MesaDTO(m.getId(), m.getNumero(), m.getCapacidade(), m.getStatus()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void atualizarMesa(Long id, MesaDTO mesaDTO) {
        MesaModel mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new MesaException("Mesa não encontrada com ID: " + id));
        if (!mesa.getNumero().equals(mesaDTO.numero()) && mesaRepository.existsByNumero(mesaDTO.numero())) {
            throw new MesaException("Número da mesa já existe");
        }
        mesa.setNumero(mesaDTO.numero());
        mesa.setCapacidade(mesaDTO.capacidade());
        mesa.setStatus(mesaDTO.status());
        mesaRepository.save(mesa);
    }

    @Transactional
    public void excluirMesa(Long id) {
        if (!mesaRepository.existsById(id)) {
            throw new MesaException("Mesa não encontrada com ID: " + id);
        }
        mesaRepository.deleteById(id);
    }
}