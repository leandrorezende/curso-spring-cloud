package br.com.fortbit.microservices.currencyexchangeservice;

import org.springframework.data.jpa.repository.JpaRepository;

interface ExchangeValueRepository extends JpaRepository<ExchangeValue, Long>{
	
	ExchangeValue findByFromAndTo(String from, String to);

}
