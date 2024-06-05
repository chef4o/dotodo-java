package com.pago.dotodo.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pago.dotodo.model.enums.RoleEnum;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class AppBeanConfiguration {
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addConverter(new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(MappingContext<String, LocalDate> mappingContext) {
                return LocalDate
                        .parse(mappingContext.getSource(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        });

        modelMapper.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(MappingContext<String, LocalDateTime> mappingContext) {
                return LocalDateTime
                        .parse(mappingContext.getSource(),
                                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            }
        });

        modelMapper.addConverter(new Converter<String, RoleEnum>() {
            @Override
            public RoleEnum convert(MappingContext<String, RoleEnum> mappingContext) {
                return RoleEnum.valueOf(mappingContext.getSource().toUpperCase());
            }
        });

        modelMapper.addConverter(new Converter<RoleEnum, String>() {
            @Override
            public String convert(MappingContext<RoleEnum, String> mappingContext) {
                return mappingContext.getSource()
                        .toString()
                        .substring(0, 1)
                        .toUpperCase()
                        + mappingContext.getSource()
                        .toString()
                        .substring(1)
                        .toLowerCase()
                        .replaceAll("_", "\s");
            }
        });

        return modelMapper;
    }
}
