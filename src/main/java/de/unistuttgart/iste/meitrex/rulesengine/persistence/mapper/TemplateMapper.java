package de.unistuttgart.iste.meitrex.rulesengine.persistence.mapper;

import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.TemplateEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemplateMapper {

    private final ModelMapper modelMapper;

}
