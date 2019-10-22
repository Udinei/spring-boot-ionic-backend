package com.udineisilva.cursomc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/** Essa classe permite a configuração de acesso ao servidor de imagens do S3 da amazon 
 *  usuario, senha, chave de acesso, etc.. serão recuperados pela anotação @Value do arquivo application.properties
 *  */
@Configuration
@PropertySource({ "classpath:env/s3amazon-${ambiente:local}.properties" }) // busca no arquivo local do sistema, se nao achar nesse endereco, executa o proximo @PropertySource
@PropertySource( value = { "file:${USERPROFILE}/.s3amazon.cursomc/s3amazon-cred.properties"}, ignoreResourceNotFound = true) // busca na pasta do sistema do usuario logado
public class S3Config {
	
	private String awsId;

	private String awsKey;
	
	// injeta objeto que acessa o classpath da aplicacao env
	@Autowired
	private Environment env;
		
	@Value("${spring.profiles.active}")
	private String profile;

	@Value("${aws.secret_access_key}")
	private String awsKeyProd;

	@Value("${aws.access_key_id}")
	private String awsIdProd;
		
	@Value("${s3.region}")
	private String region;

	@Bean
	public AmazonS3 s3client() {
		
		if(profile.equals("prod")){
			awsId =awsIdProd;
			awsKey =  awsKeyProd;
			
		}else if(profile.equals("dev") || profile.equals("test")){
			// obtendo localmente as chaves de acesso do S3 Amazon
			awsId = env.getProperty("aws.access_key_id");
			awsKey = env.getProperty("aws.secret_access_key");
		}
		
		
		BasicAWSCredentials awsCred = new BasicAWSCredentials(awsId, awsKey);
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
							.withCredentials(new AWSStaticCredentialsProvider(awsCred)).build();
		return s3client;
	}
	

}
