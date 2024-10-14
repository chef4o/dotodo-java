package com.pago.dotodo.common.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pago.dotodo.common.util.CustomStringUtil;
import com.pago.dotodo.common.util.DateTimeUtil;
import com.pago.dotodo.note.model.dto.NoteDto;
import com.pago.dotodo.note.model.dto.NoteEditDto;
import com.pago.dotodo.note.model.entity.NoteEntity;
import com.pago.dotodo.user.model.dto.EditUserProfile;
import com.pago.dotodo.user.model.dto.UserProfileView;
import com.pago.dotodo.user.model.entity.RoleEntity;
import com.pago.dotodo.user.model.entity.UserEntity;
import com.pago.dotodo.user.model.enums.RoleEnum;
import com.pago.dotodo.user.repository.RoleRepository;
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
    private final CustomStringUtil customStringUtil;

    @Autowired
    public AppBeanConfiguration(DateTimeUtil dateTimeUtil, RoleRepository roleRepository, CustomStringUtil customStringUtil) {
        this.dateTimeUtil = dateTimeUtil;
        this.roleRepository = roleRepository;
        this.customStringUtil = customStringUtil;
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
                return RoleEnum.valueOf(customStringUtil.convertToEnum(mappingContext.getSource()));
            }
        });

        modelMapper.addConverter(new Converter<RoleEnum, String>() {
            @Override
            public String convert(MappingContext<RoleEnum, String> mappingContext) {
                if (mappingContext.getSource() == null) {
                    return null;
                }
                return customStringUtil.normalize(mappingContext.getSource().name());
            }
        });

        modelMapper.addConverter(new Converter<String, RoleEntity>() {
            @Override
            public RoleEntity convert(MappingContext<String, RoleEntity> mappingContext) {
                if (mappingContext.getSource() == null || mappingContext.getSource().isEmpty()) {
                    return null;
                }
                RoleEnum roleEnum = RoleEnum.valueOf(customStringUtil.convertToEnum(mappingContext.getSource()));
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
                return customStringUtil.normalize(mappingContext.getSource().getRole().name());
            }
        });

        modelMapper.addConverter(new Converter<NoteEntity, NoteDto>() {
            @Override
            public NoteDto convert(MappingContext<NoteEntity, NoteDto> context) {
                NoteEntity source = context.getSource();
                NoteDto destination = new NoteDto();
                destination.setId(source.getId())
                        .setTitle(source.getTitle())
                        .setContent(source.getContent());

                if (source.getDueDate() != null) {
                    destination.setDueDate(dateTimeUtil.formatDateToString(source.getDueDate()))
                            .setDueTime(dateTimeUtil.formatTimeToString(source.getDueDate()));
                }

                destination.setDueDateOnly(source.getDueDateOnly())
                        .setOwnerId(source.getOwner().getId());

                return destination;
            }
        });

        modelMapper.addConverter(new Converter<NoteEntity, NoteEditDto>() {
            @Override
            public NoteEditDto convert(MappingContext<NoteEntity, NoteEditDto> context) {
                NoteEntity source = context.getSource();
                NoteEditDto destination = new NoteEditDto();
                destination.setTitle(source.getTitle())
                        .setContent(source.getContent());

                if (source.getDueDate() != null) {
                    destination.setDueDate(dateTimeUtil.formatDateToString(source.getDueDate()))
                            .setDueTime(dateTimeUtil.formatTimeToString(source.getDueDate()));
                }

                destination.setDueDateOnly(source.getDueDateOnly());

                return destination;
            }
        });

        modelMapper.addConverter(new Converter<UserEntity, UserProfileView>() {
            @Override
            public UserProfileView convert(MappingContext<UserEntity, UserProfileView> context) {
                UserEntity source = context.getSource();
                UserProfileView destination = new UserProfileView();

                destination.setFirstName(source.getFirstName())
                        .setLastName(source.getLastName())
                        .setUsername(source.getUsername())
                        .setEmail(source.getEmail())
                        .setImageUrl(source.getImageUrl())
                        .setPhoneNumber(source.getPhoneNumber());

                if (source.getDateOfBirth() != null) {
                    destination.setDob(source
                            .getDateOfBirth()
                            .format(DateTimeFormatter.ofPattern("d MMMM yyyy")));
                }

                destination.setFullName();

                if (source.getAddress() != null) {
                    destination.setAddress(source.getAddress().toString());
                }

                return destination;
            }
        });

        modelMapper.addConverter(new Converter<UserEntity, EditUserProfile>() {
            @Override
            public EditUserProfile convert(MappingContext<UserEntity, EditUserProfile> context) {
                UserEntity source = context.getSource();
                EditUserProfile destination = new EditUserProfile();

                destination.setFirstName(source.getFirstName())
                        .setLastName(source.getLastName())
                        .setUsername(source.getUsername())
                        .setEmail(source.getEmail())
                        .setImageUrl(source.getImageUrl())
                        .setPhoneNumber(source.getPhoneNumber());

                if (source.getDateOfBirth() != null) {
                    destination.setDob(source
                            .getDateOfBirth()
                            .format(DateTimeFormatter.ofPattern("d MMMM yyyy")));
                }

                if (source.getAddress() != null) {
                    if (source.getAddress().getTown() != null) {
                        destination.setTown(source.getAddress().getTown());
                    }

                    if (source.getAddress().getStreet() != null) {
                        destination.setStreet(source.getAddress().getStreet());
                    }
                }

                return destination;
            }
        });

        return modelMapper;
    }
}
