package br.com.agrotis.painel.service;

import br.com.agrotis.painel.dto.CadastroResumo;
import br.com.agrotis.painel.dto.ProdutorRequest;
import br.com.agrotis.painel.dto.ProdutorResponse;
import br.com.agrotis.painel.entity.Laboratorio;
import br.com.agrotis.painel.entity.Produtor;
import br.com.agrotis.painel.entity.Propriedade;
import br.com.agrotis.painel.exception.InscricaoDuplicadaException;
import br.com.agrotis.painel.exception.NaoEncontradoException;
import br.com.agrotis.painel.exception.RegraNegocioException;
import br.com.agrotis.painel.repository.EntidadeAgricolaRepository;
import br.com.agrotis.painel.repository.LaboratorioRepository;
import br.com.agrotis.painel.repository.ProdutorRepository;
import br.com.agrotis.painel.repository.PropriedadeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProdutorService {

    private final ProdutorRepository produtorRepository;
    private final LaboratorioRepository laboratorioRepository;
    private final PropriedadeRepository propriedadeRepository;
    private final EntidadeAgricolaRepository entidadeAgricolaRepository;
    private final GeradorDeCodigo geradorDeCodigo;

    public ProdutorService(
            ProdutorRepository produtorRepository,
            LaboratorioRepository laboratorioRepository,
            PropriedadeRepository propriedadeRepository,
            EntidadeAgricolaRepository entidadeAgricolaRepository,
            GeradorDeCodigo geradorDeCodigo
    ) {
        this.produtorRepository = produtorRepository;
        this.laboratorioRepository = laboratorioRepository;
        this.propriedadeRepository = propriedadeRepository;
        this.entidadeAgricolaRepository = entidadeAgricolaRepository;
        this.geradorDeCodigo = geradorDeCodigo;
    }

    @Transactional
    public ProdutorResponse cadastrar(ProdutorRequest request) {
        String inscricao = texto(request.inscricaoFiscal());
        validarDatas(request);
        if (entidadeAgricolaRepository.existsByInscricaoFiscal(inscricao)) {
            throw new InscricaoDuplicadaException();
        }

        Laboratorio laboratorio = buscarLaboratorio(request.laboratorioId());
        List<Propriedade> propriedades = buscarPropriedades(request.propriedadesIds());

        Produtor produtor = new Produtor();
        produtor.setCodigo(geradorDeCodigo.proximo(TipoCodigo.PRODUTOR));
        preencher(produtor, request, inscricao, laboratorio, propriedades);
        return resposta(produtorRepository.save(produtor));
    }

    @Transactional(readOnly = true)
    public List<ProdutorResponse> listar() {
        return produtorRepository.listarParaGrade().stream()
                .map(this::resposta)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutorResponse buscar(UUID id) {
        return resposta(buscarProdutor(id));
    }

    @Transactional
    public ProdutorResponse atualizar(UUID id, ProdutorRequest request) {
        Produtor produtor = buscarProdutor(id);
        String inscricao = texto(request.inscricaoFiscal());
        validarDatas(request);
        if (entidadeAgricolaRepository.existsOutraComInscricao(inscricao, id)) {
            throw new InscricaoDuplicadaException();
        }

        Laboratorio laboratorio = buscarLaboratorio(request.laboratorioId());
        List<Propriedade> propriedades = buscarPropriedades(request.propriedadesIds());
        preencher(produtor, request, inscricao, laboratorio, propriedades);
        return resposta(produtor);
    }

    @Transactional
    public void excluir(UUID id) {
        Produtor produtor = buscarProdutor(id);
        produtorRepository.delete(produtor);
    }

    private void preencher(
            Produtor produtor,
            ProdutorRequest request,
            String inscricao,
            Laboratorio laboratorio,
            List<Propriedade> propriedades
    ) {
        produtor.setNome(texto(request.nome()));
        produtor.setInscricaoFiscal(inscricao);
        produtor.setEndereco(texto(request.endereco()));
        produtor.setDataInicial(request.dataInicial());
        produtor.setDataFinal(request.dataFinal());
        produtor.setObservacoes(limparOpcional(request.observacoes()));
        produtor.setComissaoProducao(request.comissaoProducao());
        produtor.setLaboratorio(laboratorio);
        produtor.getPropriedades().clear();
        produtor.getPropriedades().addAll(propriedades);
    }

    private Produtor buscarProdutor(UUID id) {
        return produtorRepository.buscarComVinculos(id)
                .orElseThrow(() -> new NaoEncontradoException("Produtor não encontrado"));
    }

    private Laboratorio buscarLaboratorio(UUID id) {
        return laboratorioRepository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Laboratório não encontrado"));
    }

    private List<Propriedade> buscarPropriedades(List<UUID> idsInformados) {
        List<UUID> ids = idsInformados == null ? List.of() : idsInformados.stream().distinct().toList();
        if (ids.stream().anyMatch(Objects::isNull)) {
            throw new RegraNegocioException("propriedadesIds", "UUID inválido");
        }
        if (ids.isEmpty()) {
            return List.of();
        }

        List<Propriedade> encontradas = propriedadeRepository.findAllById(ids);
        if (encontradas.size() != ids.size()) {
            throw new NaoEncontradoException("Propriedade não encontrada");
        }
        return encontradas;
    }

    private void validarDatas(ProdutorRequest request) {
        if (request.dataFinal().isBefore(request.dataInicial())) {
            throw new RegraNegocioException("dataFinal", "anterior à data inicial");
        }
    }

    private ProdutorResponse resposta(Produtor produtor) {
        List<CadastroResumo> propriedades = produtor.getPropriedades().stream()
                .sorted(Comparator.comparing(Propriedade::getCodigo))
                .map(CadastroResumo::de)
                .toList();

        return new ProdutorResponse(
                produtor.getId(),
                produtor.getCodigo(),
                produtor.getNome(),
                produtor.getInscricaoFiscal(),
                produtor.getEndereco(),
                produtor.getDataInicial(),
                produtor.getDataFinal(),
                produtor.getObservacoes(),
                produtor.getComissaoProducao(),
                CadastroResumo.de(produtor.getLaboratorio()),
                propriedades
        );
    }

    private String texto(String valor) {
        return valor.trim();
    }

    private String limparOpcional(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.trim();
    }
}
