package com.pago.dotodo.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pago.dotodo.model.dto.NoteDto;
import com.pago.dotodo.model.dto.NoteEditDto;
import com.pago.dotodo.model.entity.NoteEntity;
import com.pago.dotodo.model.entity.RoleEntity;
import com.pago.dotodo.model.enums.RoleEnum;
import com.pago.dotodo.repository.RoleRepository;
import com.pago.dotodo.util.DateTimeUtil;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Configuration
public class AppBeanConfiguration {

    private final DateTimeUtil dateTimeUtil;
    private final RoleRepository roleRepository;

    @Autowired
    public AppBeanConfiguration(DateTimeUtil dateTimeUtil, RoleRepository roleRepository) {
        this.dateTimeUtil = dateTimeUtil;
        this.roleRepository = roleRepository;
    }

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
                String source = mappingContext.getSource();
                try {
                    return LocalDateTime.parse(source, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                } catch (DateTimeParseException e) {
                    try {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate localDate = LocalDate.parse(source, dateFormatter);
                        return localDate.atStartOfDay();
                    } catch (DateTimeParseException ex) {
                        throw new IllegalArgumentException("Invalid date format: " + source, ex);
                    }
                }
            }
        });

        modelMapper.addConverter(new Converter<String, RoleEnum>() {
            @Override
            public RoleEnum convert(MappingContext<String, RoleEnum> mappingContext) {
                if (mappingContext.getSource() == null || mappingContext.getSource().isEmpty()) {
                    return null;
                }
                return RoleEnum.valueOf(mappingContext.getSource().toUpperCase());
            }
        });

        modelMapper.addConverter(new Converter<RoleEnum, String>() {
            @Override
            public String convert(MappingContext<RoleEnum, String> mappingContext) {
                if (mappingContext.getSource() == null) {
                    return null;
                }
                return mappingContext.getSource().toString().substring(0, 1).toUpperCase()
                        + mappingContext.getSource().toString()
                        .substring(1).toLowerCase()
                        .replaceAll("_", " ");
            }
        });

        modelMapper.addConverter(new Converter<String, RoleEntity>() {
            @Override
            public RoleEntity convert(MappingContext<String, RoleEntity> mappingContext) {
                if (mappingContext.getSource() == null || mappingContext.getSource().isEmpty()) {
                    return null;
                }
                RoleEnum roleEnum = RoleEnum.valueOf(mappingContext.getSource().toUpperCase());
                Optional<RoleEntity> roleEntityOptional = roleRepository.findRoleEntityByRole(roleEnum);
                return roleEntityOptional.orElse(null);
            }
        });

        modelMapper.addConverter(new Converter<RoleEntity, String>() {
            @Override
            public String convert(MappingContext<RoleEntity, String> mappingContext) {
                if (mappingContext.getSource() == null) {
                    return null;
                }
                return mappingContext.getSource().getRole().toString();
            }
        });

        modelMapper.addConverter(new Converter<NoteEntity, NoteDto>() {
            @Override
            public NoteDto convert(MappingContext<NoteEntity, NoteDto> context) {
                NoteEntity source = context.getSource();
                NoteDto destination = new NoteDto();
                destination.setId(source.getId());
                destination.setTitle(source.getTitle());
                destination.setContent(source.getContent());

                if (source.getDueDate() != null) {
                    destination.setDueDate(dateTimeUtil.formatDateToString(source.getDueDate()));
                    destination.setDueTime(dateTimeUtil.formatTimeToString(source.getDueDate()));
                }

                destination.setDueDateOnly(source.getDueDateOnly());
                destination.setOwnerId(source.getOwner().getId());

                return destination;
            }
        });

        modelMapper.addConverter(new Converter<NoteEntity, NoteEditDto>() {
            @Override
            public NoteEditDto convert(MappingContext<NoteEntity, NoteEditDto> context) {
                NoteEntity source = context.getSource();
                NoteEditDto destination = new NoteEditDto();
                destination.setTitle(source.getTitle());
                destination.setContent(source.getContent());

                if (source.getDueDate() != null) {
                    destination.setDueDate(dateTimeUtil.formatDateToString(source.getDueDate()));
                    destination.setDueTime(dateTimeUtil.formatTimeToString(source.getDueDate()));
                }

                destination.setDueDateOnly(source.getDueDateOnly());

                return destination;
            }
        });

        return modelMapper;
    }
}
