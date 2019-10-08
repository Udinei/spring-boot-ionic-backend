package com.udineisilva.cursomc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/** Essa classe permite a configuração de acesso ao servidor de imagens do S3 da amazon 
 *  usuario, senha, chave de acesso, etc.. serão recuperados pela anotação @Value do arquivo application.properties
 *  */
@Configuration
public class S3Config {
	
	@Value("${aws.access_key_id}")
	private String awsId;

	@Value("${aws.secret_access_key}")
	private String awsKey;

	@Value("${s3.region}")
	private String region;

	@Bean
	public AmazonS3 s3client() {
		BasicAWSCredentials awsCred = new BasicAWSCredentials(awsId, awsKey);
		AmazonS3 s3client = AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
							.withCredentials(new AWSStaticCredentialsProvider(awsCred)).build();
		return s3client;
	}

}
