package com.udineisilva.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {
	
	// para tratar caso o parametro venha com espaço caracter (%20)
	public static String decodeParam(String s){
		try {
			return URLDecoder.decode(s, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
				return "";
		}
	}
	
	public static List<Integer> decodeIntList(String s){
		String[] vet = s.split(",");
		List<Integer> list = new ArrayList<>();
		
		for(int i=0; i < vet.length; i++){
			list.add(Integer.parseInt(vet[i]));
		}
		
		// O codigo abaixo representa o mesmo codigo acima
		//return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt((String) x)).collect(Collectors.toList());		
		
		return list;

	}

}
