package br.com.wepdev.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import br.com.wepdev.model.Categoria_;
import br.com.wepdev.model.Lancamento;
import br.com.wepdev.model.Lancamento_;
import br.com.wepdev.model.Pessoa_;
import br.com.wepdev.repository.filter.LancamentoFilter;
import br.com.wepdev.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager; // Injetado para poder ser feita as consultas

	/**
	 * Metodo implementado com a criteria do JPA, a do hibernate foi depreciada
	 *
	 * Pageable pageable -> e possivel fazer a paginação passando os campos size (quantidade de elementos que serão retornados) ,
	 * page (a pagina dos elementos que serão retornados)
	 * @param lancamentoFilter
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {

		CriteriaBuilder builder = manager.getCriteriaBuilder(); // constroi as criterias
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);

		// Criando os filtros
		Root<Lancamento> root = criteria.from(Lancamento.class); // Pegando os atributos de lançamneto para fazer o filtro

		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);

		criteria.where(predicates); // passando o whare da consulta

		TypedQuery<Lancamento> query = manager.createQuery(criteria); // criacao da query

		adicionarRestricoesDePaginacao(query, pageable);

		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter)) ;
	}

	/**
	 * Metodo responsavel por retornar no endPoint do resumo de lancamentos os campos desejados
	 *
	 * @param lancamentoFilter
	 * @param pageable
	 * @return
	 */
	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		criteria.select(builder.construct(ResumoLancamento.class
				, root.get(Lancamento_.codigo)
				, root.get(Lancamento_.descricao)
				, root.get(Lancamento_.dataVencimento)
				, root.get(Lancamento_.dataPagamento)
				, root.get(Lancamento_.valor)
				, root.get(Lancamento_.tipo)
				, root.get(Lancamento_.categoria).get(Categoria_.nome)
				, root.get(Lancamento_.pessoa).get(Pessoa_.nome)));
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}


	/**
	 *
	 * @param lancamentoFilter
	 * @return
	 */
	private Long total(LancamentoFilter lancamentoFilter) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class); // retorna um long na consulta
		Root<Lancamento> root = criteria.from(Lancamento.class); // fazendo a consulta em lancamento
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		criteria.select(builder.count(root)); // contando a quantidade de registros
		// o select de count retorna apenas 1 resultado por isso o getSingleResult()
		return manager.createQuery(criteria).getSingleResult();
	}
	

	/*
	   Metodo que adiciona restrições de paginação com size e page de acordo com o solicitado na request
	 * TypedQuery<?> com a ? ele aceita qualquer objeto, deixando mais generico
	 */
	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {

		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();

		/**
		 * logica para os primeiros registros da pagina que serao mostrados
		 */
		int primeiroRegistroDapagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDapagina); // primeiros regustros da pagina que serão mostrados
		query.setMaxResults(totalRegistrosPorPagina); // total de registros que sera most4rado por pagina
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		
		List<Predicate> predicates = new ArrayList<>(); // lista que sera adicionada os filtros da pesquisa

		if (!ObjectUtils.isEmpty(lancamentoFilter.getDescricao())) { // se foi passada a descricao no filtro
			predicates.add(builder.like(  // Adicionado de criterio, a descricao

					// like-> passando o conteudo da descricao na consulta
					//builder.lower(root.get("descricao")), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));

					//lower() -> deixando a descricao passada em letras minusculas
					//"%" + lancamentoFilter.getDescricao().toLowerCase() + "%" -> pegando o valor do campo descricao

			        /*
			        Utilizando MetaModel - Algaworks aula 5.7 Implementando pesquisa de lançamento com Metamodel.

			        root.get("descricao") -> dessa forma o nome descricao pode ser escrito de forma errada e gerar um erro
			        para evitar isso vms utilizar o MetaModel, ele analisa a entidade e cria uma classe com os atributos
			        estaticos, ou seja se algum dos campos for modificados o metamodel atualiza automaticamente eles.
			        root.get(Lancamento_.descricao) -> utilizando o metamodel. para descobrir como fazer e so pesquisar
			        JPA MetaModel.
			        A Classe metaModel Lancamento_ foi criada em target/generated-sources/annotations/....
			        jpamodelgen -> dependencia adicionada no pom.xml
			         */
			        builder.lower(root.get("descricao")), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}

		if (lancamentoFilter.getDataVencimentoDe() != null) {
			predicates.add(
					builder.greaterThanOrEqualTo(root.get("dataVencimento"), lancamentoFilter.getDataVencimentoDe()));
			        //greaterThanOrEqualTo() -> MAIOR OU IGUAL dataVencimentoDe
		}

		if (lancamentoFilter.getDataVencimentoAte() != null) {
			predicates.add(
					builder.lessThanOrEqualTo(root.get("dataVencimento"), lancamentoFilter.getDataVencimentoAte()));
			        // lessThanOrEqualTo() -> MENOR OU IGUAL dataVencimentoAte
		}
		
		// Transformando predicates em um array e 
		return predicates.toArray(new Predicate[predicates.size()]);
	}

}
