package com.projetospring.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.apache.catalina.authenticator.jaspic.PersistentProviderRegistrations.Property;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.context.annotation.Bean;

public class Utils {

    public static void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source)); // Copia todas as propriedades do objeto de origem para o objeto de destino
    }

    public static String [] getNullPropertyNames(Object source){ // Método para obter os nomes das propriedades que são nulas no objeto de origem
        final BeanWrapper src = new BeanWrapperImpl(source); // Cria um BeanWrapper para o objeto de origem
        
        PropertyDescriptor[] pds = src.getPropertyDescriptors(); // Obtém os descritores de propriedades do objeto 
    
        Set<String> emptyNames = new HashSet<>(); // Cria um conjunto para armazenar os nomes das propriedades vazias
        for (PropertyDescriptor pd : pds) {
           Object srcValue = src.getPropertyValue(pd.getName());
           if (srcValue == null) {
               emptyNames.add(pd.getName());
           }
        }

        String[] result = new String[emptyNames.size()]; // Cria um array de strings com o tamanho do conjunto de nomes vazios
        return emptyNames.toArray(result); // Converte o conjunto de nomes vazios para um array e o retorna
    
    }
}
