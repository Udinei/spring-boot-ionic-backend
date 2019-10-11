package com.udineisilva.cursomc;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.udineisilva.cursomc.services.S3Service;

public class S3ServiceTest extends CursomcApplicationTests {
	
	private Logger LOG = LoggerFactory.getLogger(S3Service.class);
	
	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	private static String pathTest;
	
	private static String pathSystem;
	
	
	@BeforeClass
	public static void setUp() throws Exception {
		/** caminho relativo da pasta de testes */ 
		pathTest = new File("/src/test/java").getCanonicalPath().substring(2);
		
		/** caminho absoluto do sistema */
		pathSystem = new File(".").getCanonicalPath(); 
	}
	
	
	@Test
	public void deveEnviarArquivoTesteJpgParaS3Amazon() throws IOException{
			uploadFile(pathSystem + pathTest + "/images/ImgTesteLoadS3.jpg");
	             
	}
	
	public void uploadFile(String localFilePath) {
		try {
			File file = new File(localFilePath);
			LOG.info("Iniciando upload");
			s3client.putObject(new PutObjectRequest(bucketName, "ImgTesteLoadS3.jpg", file));
			LOG.info("Upload finalizado");
		}
		catch (AmazonServiceException e) {
			LOG.info("AmazonServiceException: " + e.getErrorMessage());
			LOG.info("Status code: " + e.getErrorCode());
		}
		catch (AmazonClientException e) {
			LOG.info("AmazonClientException: " +  e.getMessage());
		}
	}
}


