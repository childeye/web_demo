package com.uangel.svc.demo.web.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.google.gson.GsonBuilder;
import com.uangel.svc.demo.web.model.User;
import com.uangel.svc.demo.web.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureRestDocs// 기본값 (uriScheme = "http", uriHost = "localhost", uriPort = 8080)
public class UserControllerTest {
	
	// https://docs.spring.io/spring-restdocs/docs/2.0.4.RELEASE/reference/html5/#getting-started-sample-applications
	// maven 에서는 MockMvc 만 지원.
	@Autowired
	private MockMvc mockMvc;
	
	// @WebMvcTest 는 Controller 만 로딩하기때문에 Controller 에서 사용하는 Service 를 로딩해야 한다.
	@MockBean 
	private UserService userService;
	
	FieldDescriptor[] userField = new FieldDescriptor[] {
			fieldWithPath(".id").type(JsonFieldType.NUMBER).description("user id"),
            fieldWithPath(".name").description("user name"),
            fieldWithPath(".age").type(JsonFieldType.NUMBER).description("user age").optional()};
	
	
	
	/*
	@RegisterExtension
	final RestDocumentationExtension restDocumentation = new RestDocumentationExtension();
	
	@BeforeEach
	public void setUp(WebApplicationContext webApplicationContext,
			RestDocumentationContextProvider restDocumentation) {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(
						documentationConfiguration(restDocumentation)
						.uris()
						.withScheme("http")
						.withHost("localhost")
						.withPort(8080)
				)
				.build();
	}
	*/
	
	@Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/user"))
            .andExpect(status().isOk());
//            .andExpect(content().string(containsString("Hello, World")));
    }
	
	@Test
	public void 사용자_조회() throws Exception {
		mockMvc.perform(RestDocumentationRequestBuilders.get("/user/{id}", 0)
			.accept(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk());
		/*
			.andDo(document("user", // <output-directory>/user/curl-request.adoc, http-request.adoc, http-response.adoc, httpie-request.adoc, request-body.adoc, response-body.adoc 생성됨.
					pathParameters(
						org.springframework.restdocs.request.RequestDocumentation.parameterWithName("id").description("user unique id") // request의 파라미터 필드, response의 필드의 설명을 적어줌으로써 이 정보를 가지고 snippets 가 생성이 되고 결과적으로 API 문서가 만들어 진다.
				),
				responseFields(
//					fieldWithPath("id").description("user unique id"),
//					fieldWithPath("name").description("user name"),
//					fieldWithPath("age").type(Integer.class).description("user age")
						user
				)
			))
			.andExpect(jsonPath("id", is(notNullValue()))) // 이 부분에서 테스트를 동시에 함으로써 응답이 달라지거나 잘못된 응답이 내려올 경우 TestCase가 실패하게 되어 API문서 또한 생성되지 않게 된다.
			.andExpect(jsonPath("name", is(notNullValue())))
			.andExpect(jsonPath("age", is(notNullValue())));
			*/
	}
	
	@Test
	public void 사용자_등록() throws Exception {
		User user = User.builder().id(1).name("홍길동").age(20).build();
		//String jsonString = new GsonBuilder().setPrettyPrinting().create().toJson(user);
		
		// given
		// 예상응답값. 실행해줘야 responseFields 가 가능하다.
		// given 실행이 없으면 body empty 로 인식함.
		given(userService.add(user)).willReturn(user);
		
		// when
		String jsonString = new GsonBuilder().create().toJson(user);
		
		ResultActions result = mockMvc.perform(
				post("/user")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonString)
                .accept(MediaType.APPLICATION_JSON_UTF8))
				.andDo(print());
		
		// then
		result.andExpect(status().isOk())
                .andDo(document("user/{method-name}",
                		preprocessRequest(modifyUris().scheme("https").host("docs.api.com").removePort()),
                		//preprocessRequest(prettyPrint()),
                		//preprocessResponse(prettyPrint()),
                        requestFields(userField),
                        responseFields(userField)
                ));
	}
	
	@Test
    public void 사용자_리스트_조회() throws Exception {
        mockMvc.perform(
                get("/user")
//                        .param("page", "1")
//                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(status().isOk());
                /*
                .andDo(document("user",
//                        requestParameters(
//                                parameterWithName("page").description("페이지 번호"),
//                                parameterWithName("size").description("페이지 사이즈")
//                        ),
                        responseFields(
//                                fieldWithPath("[].id").description("user id"),
//                                fieldWithPath("[].name").description("user name"),
//                                fieldWithPath("[].age").type(Integer.class).description("user age")
                        		fieldWithPath("[]").description("user list")
                        ).andWithPrefix("[]", user)
                ));
//                .andExpect(jsonPath("[0].id", is(notNullValue())))
//                .andExpect(jsonPath("[0].name", is(notNullValue())))
//                .andExpect(jsonPath("[0].age", is(notNullValue())));
 * 
 */
    }

}
