package me.whiteship.demorestapi.event;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_VALUE)
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors){

        if(errors.hasErrors()){
//            return ResponseEntity.badRequest().body(errors);
            return badRequest(errors);
        }

        eventValidator.validate(eventDto, errors);

        if(errors.hasErrors()){
//            return ResponseEntity.badRequest().body(errors);
            return badRequest(errors);
        }

        Event event = modelMapper.map(eventDto, Event.class);
        event.update();
        final Event newEvent = this.eventRepository.save(event);

        final WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(newEvent.getId());
        final URI createdUri = selfLinkBuilder.toUri();

        //event.setId(10);
        final EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        //eventResource.add(selfLinkBuilder.withSelfRel());
        eventResource.add(selfLinkBuilder.withRel("update-event"));
        eventResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));

        return ResponseEntity.created(createdUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assembler){

        final Page<Event> page = this.eventRepository.findAll(pageable);
        var pageResources = assembler.toModel(page, e -> new EventResource(e));
        pageResources.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));
        return ResponseEntity.ok(pageResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id){
        final Optional<Event> optionalEvent = this.eventRepository.findById(id);

        if (optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        final Event event = optionalEvent.get();
        final EventResource eventResource = new EventResource(event);
        eventResource.add(new Link("/docs/index.html#resources-events-get").withRel("profile"));
        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id,
                                      @RequestBody @Valid EventDto eventDto,
                                      Errors errors){
        final Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if(optionalEvent.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        if(errors.hasErrors()){
            return badRequest(errors);
        }

        this.eventValidator.validate(eventDto, errors);

        if(errors.hasErrors()){
            return badRequest(errors);
        }

        final Event existingEvent = optionalEvent.get();
        this.modelMapper.map(eventDto, existingEvent);
        final Event savedEvent = this.eventRepository.save(existingEvent);

        final EventResource eventResource = new EventResource(savedEvent);
        eventResource.add(new Link("/docs/index.html#resources-events-update").withRel("profile"));

        return ResponseEntity.ok(eventResource);
    }


    private ResponseEntity<ErrorsResource> badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }
}
