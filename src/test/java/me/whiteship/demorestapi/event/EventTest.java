package me.whiteship.demorestapi.event;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                    .name("Inflearn Spring REST API")
                    .description("REST API development with Spring")
                    .build();

        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        //given
        String name = "Event";
        final String description = "Spring";

        //when
        Event event = new Event();
        event.setName("Event");
        event.setDescription("Spring");

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);

    }

    @ParameterizedTest
    @MethodSource("parametersForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        //given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        //when
        event.update();
        //then
        //assertThat(event.isFree()).isTrue();
        assertThat(event.isFree()).isEqualTo(isFree);

//        //given
//        event = Event.builder()
//                .basePrice(100)
//                .maxPrice(0)
//                .build();
//        //when
//        event.update();
//        //then
//        assertThat(event.isFree()).isFalse();
//
//        //given
//        event = Event.builder()
//                .basePrice(0)
//                .maxPrice(100)
//                .build();
//        //when
//        event.update();
//        //then
//        assertThat(event.isFree()).isFalse();

    }

    private static Stream<Arguments> parametersForTestFree(){

        return Stream.of(
                Arguments.of(0,0, true),
                Arguments.of(100,0, false),
                Arguments.of(0,100, false)
        );
    }


    @ParameterizedTest
    @MethodSource("parametersForTestOffline")
    public void testOffline(String location, boolean isOffline){
        //given
        Event event = Event.builder()
                .location(location)
                .build();
        //when
        event.update();
        //then
//        assertThat(event.isOffline()).isTrue();
        assertThat(event.isOffline()).isEqualTo(isOffline);

//        //given
//        event = Event.builder()
//                .build();
//        //when
//        event.update();
//        //then
//        assertThat(event.isOffline()).isFalse();
    }


    private static Stream<Arguments> parametersForTestOffline(){

        return Stream.of(
                Arguments.of("강남역 네이버 D2 스타텁 팩토리", true),
                Arguments.of(null, false),
                Arguments.of("      ", false)
        );
    }



}