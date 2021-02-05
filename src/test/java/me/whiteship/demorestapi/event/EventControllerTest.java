package me.whiteship.demorestapi.event;

import me.whiteship.demorestapi.accounts.Account;
import me.whiteship.demorestapi.accounts.AccountRepository;
import me.whiteship.demorestapi.accounts.AccountRole;
import me.whiteship.demorestapi.accounts.AccountService;
import me.whiteship.demorestapi.common.AppProperties;
import me.whiteship.demorestapi.common.BaseControllerTest;
import me.whiteship.demorestapi.common.TestDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.common.util.Jackson2JsonParser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class EventControllerTest extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AppProperties appProperties;

    @BeforeEach
    public  void setUp(){
        this.eventRepository.deleteAll();
        this.accountRepository.deleteAll();

    }

    @Test
    @TestDescription("입력 값이 잘못된 경우에 에러가 발생하는 테스트")
    public void creatEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021,1,29, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2021,1,31,18,0))
                .beginEventDateTime(LocalDateTime.of(2021,2,3,14,0))
                .endEventDateTime(LocalDateTime.of(2021,2,1,14,0))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        this.mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                //.andExpect((ResultMatcher) -> jsonPath("$[0].objectName").exists())
                //.andExpect(jsonPath("$[0].field").exists())
                //.andExpect((ResultMatcher) -> jsonPath("$[0].defaultMessage").exists())
                //.andExpect((ResultMatcher) -> jsonPath("$[0].code").exists())
                .andExpect((ResultMatcher) -> jsonPath("errors[0].objectName").exists())
                .andExpect((ResultMatcher) -> jsonPath("errors[0].defaultMessage").exists())
                .andExpect((ResultMatcher) -> jsonPath("errors[0].code").exists())
                .andExpect((ResultMatcher) -> jsonPath("_links_index").exists())

                //.andExpect(jsonPath("$[0].rejectedValue").exists())
        ;
    }

    @Test
    @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void creatEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception{

        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021,1,30, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2021,1,31,18,0))
                .beginEventDateTime(LocalDateTime.of(2021,2,1,14,0))
                .endEventDateTime(LocalDateTime.of(2021,2,2,14,0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(true)
                .free(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        //event.setId(10);
        //Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @TestDescription("정상적으로 이벤트 생섬")
    public void createEvent() throws Exception{


        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021,1,30, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2021,1,31,18,0))
                .beginEventDateTime(LocalDateTime.of(2021,2,1,14,0))
                .endEventDateTime(LocalDateTime.of(2021,2,2,14,0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .build();

        //event.setId(10);
        //Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                    .andDo(print())
                    .andExpect(status().isCreated()) //isCreated 201
                    .andExpect(jsonPath("id").exists())
                    //.andExpect(header().exists("Location"))
                    .andExpect(header().exists(HttpHeaders.LOCATION))
                    .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                    //.andExpect(jsonPath("id").value(Matchers.not(100)))
                    .andExpect(jsonPath("free").value(false))
                    .andExpect(jsonPath("offline").value(true))
                    .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                    .andExpect(jsonPath("_links.self").exists()) //document 아래에서 테스트를 하기 때문에 제거해도 된다
                    .andExpect(jsonPath("_links.query-events").exists())
                    .andExpect(jsonPath("_links.update-event").exists())
                    .andDo(document("create-event",
                            links(
                                    linkWithRel("self").description("link to self"),
                                    linkWithRel("query-events").description("link to query events"),
                                    linkWithRel("update-event").description("link to update an existing event"),
                                    linkWithRel("profile").description("link to update an existing event")
                            ),
                            requestHeaders(
                                    headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                            ),
                            requestFields(
                                    fieldWithPath("name").description("Name of new event"),
                                    fieldWithPath("description").description("description of new event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                    fieldWithPath("closeEnrollmentDateTime").description("date time of begin of new event"),
                                    fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                    fieldWithPath("endEventDateTime").description("date time of begin of new event"),
                                    fieldWithPath("location").description("location of new event"),
                                    fieldWithPath("basePrice").description("base price of new event"),
                                    fieldWithPath("maxPrice").description("max price of new event"),
                                    fieldWithPath("limitOfEnrollment").description("limit of enrollment")

                                    ),
                            responseHeaders(
                                    headerWithName(HttpHeaders.LOCATION).description("Location header"),
                                    headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")

                            ),
                            relaxedResponseFields(
                                    fieldWithPath("id").description("identifier of new event"),
                                    fieldWithPath("name").description("Name of new event"),
                                    fieldWithPath("description").description("description of new event"),
                                    fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                    fieldWithPath("closeEnrollmentDateTime").description("date time of begin of new event"),
                                    fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
                                    fieldWithPath("endEventDateTime").description("date time of begin of new event"),
                                    fieldWithPath("location").description("location of new event"),
                                    fieldWithPath("basePrice").description("base price of new event"),
                                    fieldWithPath("maxPrice").description("max price of new event"),
                                    fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                    fieldWithPath("free").description("is tells is this event is free or not"),
                                    fieldWithPath("offline").description("is tells if this event is offline or not"),
                                    fieldWithPath("eventStatus").description("event status"),
                                    fieldWithPath("_links.self.href").description("Link to self"),
                                    fieldWithPath("_links.query-events.href").description("Link to query event list"),
                                    fieldWithPath("_links.update-event.href").description("Link to update existing event"),
                                    fieldWithPath("_links.profile.href").description("Link to profile")


                            )
                            ))
        ;

    }


    private String getBearerToken() throws Exception {
        return "Bearer " + getAccessToken();
    }

    private String getAccessToken() throws Exception {

            //Given
//            final String username = "whddn528@email.com";
//            final String password = "jongwoo";

            final Account jongwoo = Account.builder()
                    .email(appProperties.getUserUsername())
                    .password(appProperties.getUserPassword())
                    .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                    .build();

            this.accountService.saveAccount(jongwoo);

            String clientId = "myApp";
            String clientSecret = "pass";
        final ResultActions perform = this.mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                .param("username", appProperties.getUserUsername())
                .param("password", appProperties.getUserPassword())
                .param("grant_type", "password"));

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Jackson2JsonParser parser = new Jackson2JsonParser();

        return parser.parseMap(responseBody).get("access_token").toString();

    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void getEvents() throws Exception{

        //Given
        IntStream.range(0,30).forEach(this::generateEvent);

        //When
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/events")
                    .param("page","1")
                    .param("size","10")
                    .param("sort","name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))


        ;
        //Then

    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void getEventsWithAuthentication() throws Exception{

        //Given
        IntStream.range(0,30).forEach(this::generateEvent);

        //When
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/events")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .param("page","1")
                .param("size","10")
                .param("sort","name,DESC"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.create-event").exists())
                .andDo(document("query-events"))

        ;
        //Then

    }


    @Test
    @TestDescription("기존 이벤트를 하나 조회하기")
    public void getEvent() throws Exception{
        //Given
        final Event event = this.generateEvent(100);

        //When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
                ;

    }

    @Test
    @TestDescription("없는 이벤트를 조회했을 때 404 응답받기")
    public void getEvent404() throws Exception{

        //When & Then
        this.mockMvc.perform(get("/api/events/11083"))
                .andExpect(status().isNotFound());

    }

    @Test
    @TestDescription("이벤트를 정상적으로 수정하기")
    public void updateEvent() throws Exception{

        //Given
        Event event = this.generateEvent(200);
        String eventName = "update event";
        final EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        eventDto.setName(eventName);

        //When
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(eventName))
                .andExpect(jsonPath("_links.self").exists())
                ;
    }

    @Test
    @TestDescription("입력값이 잘못된 경우에 이벤트 수정 실패")
    public void updateEvent400_Wrong() throws Exception{

        //Given
        Event event = this.generateEvent(200);
        String eventName = "update event";
        final EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        eventDto.setName(eventName);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(1000);

        //When
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                    .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @TestDescription("입력값이 비어있 경우에 이벤트 수정 실패")
    public void updateEvent400_Empty() throws Exception{

        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = new EventDto();

        //When
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    @TestDescription("존재하지 않는 이벤트 수정 실패")
    public void updateEvent404() throws Exception{

        //Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        //When
        this.mockMvc.perform(put("/api/events/123123")
                .header(HttpHeaders.AUTHORIZATION, getBearerToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    private Event generateEvent(int i) {
        Event event = Event.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2021,1,30, 14, 21))
                .closeEnrollmentDateTime(LocalDateTime.of(2021,1,31,18,0))
                .beginEventDateTime(LocalDateTime.of(2021,2,1,14,0))
                .endEventDateTime(LocalDateTime.of(2021,2,2,14,0))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2 스타텁 팩토리")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();

        return this.eventRepository.save(event);
    }



}
