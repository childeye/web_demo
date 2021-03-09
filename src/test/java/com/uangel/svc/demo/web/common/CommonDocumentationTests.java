package com.uangel.svc.demo.web.common;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EnumViewController.class)
@AutoConfigureRestDocs
public class CommonDocumentationTests {

	@Autowired
    private MockMvc mockMvc;

    @Test
    public void commons() throws Exception {

        this.mockMvc.perform(
                get("/docs")  // (1)
                        .accept(MediaType.APPLICATION_JSON)
        )

                .andExpect(status().isOk())
                .andDo(document("common",
                		customResponseFields("custom-response", beneathPath("data.genders").withSubsectionId("genders"), attributes(key("title").value("성별")),
                		enumConvertFieldDescriptor(Gender.values())
                	)
                ));
    }

    private FieldDescriptor[] enumConvertFieldDescriptor(EnumType[] enumTypes) {
        return Arrays.stream(enumTypes)
                .map(enumType -> fieldWithPath(enumType.getId()).description(enumType.getText()))
                .toArray(FieldDescriptor[]::new);
    }

    public static CustomResponseFieldsSnippet customResponseFields(String type,
                                                                   PayloadSubsectionExtractor<?> subsectionExtractor,
                                                                   Map<String, Object> attributes, FieldDescriptor... descriptors) {
        return new CustomResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes
                , true);
    }
    
}
