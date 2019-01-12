package utils;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import org.redisson.client.protocol.decoder.ObjectMapDecoder;
import org.springframework.data.convert.Jsr310Converters;

import java.security.AlgorithmConstraints;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;


@Slf4j
public class jsonutils {

    private  static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        objectMapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, false);
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS,false);
        objectMapper.setDateFormat(new SimpleDateFormat(datetimeutils.standard_format));
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
    }

        public static <T> String obj2string(T obj){
           if (obj==null){
               return null;
           }
            try {
                return obj instanceof String ?(String)obj:objectMapper.writeValueAsString(obj);
            } catch (Exception e) {
                log.warn("",e);
                return  null;
            }
        }


        public static <T> String obj2stringpretty(T obj){
        if (obj==null){
            return  null;
        }
        try {
            return  obj instanceof String ? (String) obj :objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        }catch (Exception e){
            log.warn("",e);
            return null;
        }

    }




    public static <T> T string2obj(String str,Class<T> clazz){
        if (StringUtils.isEmpty(str) || clazz==null){
            return  null;

        }
        try {
            return clazz.equals(String.class)?(T)str:objectMapper.readValue(str,clazz);
        }catch (Exception e){
            log.warn("" ,e);
            return  null;
        }
    }








    public static <T> T string2obj(String str, TypeReference<T> typeReference){
        if ( StringUtils.isEmpty(str)|| typeReference==null){
            return  null;
        }
        try {
            return (T) (typeReference.getType().equals(String.class)?str:objectMapper.readValue(str,typeReference);
        }catch (Exception e){
            log.warn("");
            return null;
        }

    }




    public static <T> T string2obj (String str, Class<?> colletionclass,Class<?>... elementclasses){


        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(colletionclass,elementclasses);
        try {
            return objectMapper.readValue(str,javaType);
        }catch (Exception e){
            log.warn("");
            return  null;
        }
    }

}
