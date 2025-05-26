package br.app.appLogin.services;

import br.app.appLogin.dtos.MesaDTO;
import br.app.appLogin.exceptions.MesaException;
import br.app.appLogin.models.MesaModel;
import br.app.appLogin.repositories.MesaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MesaService {

    private static final Logger logger = LoggerFactory.getLogger(MesaService.class);
    private final MesaRepository mesaRepository;

    public MesaService(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    @Transactional(readOnly = true)
    public MesaModel buscarMesaPorId(Long id) throws MesaException {
        logger.info("Buscando mesa com ID: {}", id);
        return mesaRepository.findById(id)
                .orElseThrow(() -> new MesaException("Mesa com ID: " + id + " não encontrada!"));
    }

    @Transactional(readOnly = true)
    public MesaModel buscarMesaPorNumero(Integer numero) throws MesaException {
        logger.info("Buscando mesa com numero: {}", numero);
        return mesaRepository.findByNumero(numero)
                .orElseThrow(() -> new MesaException("Mesa com numero: " + numero + " não encontrada!"));
    }

    @Transactional
    public void cadastrarMesa(MesaDTO mesaDTO) {
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
    public void excluirMesaPorId(Long id) {
        MesaModel mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new MesaException("Mesa não encontrada com ID " + id));
        if (mesa.getStatus().equalsIgnoreCase("OCCUPIED") || mesa.getStatus().equalsIgnoreCase("RESERVED")) {
            throw new MesaException("Não é possível excluir a mesa pois ela está Ocupada/Reservada");
        }
        mesaRepository.deleteById(id);
    }
}