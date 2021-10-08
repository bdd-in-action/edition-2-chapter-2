package manning.bddinaction.steps;

import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import manning.bddinaction.itineraries.ItineraryService;
import manning.bddinaction.timetables.InMemoryTimeTable;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

public class DepartingTrainsStepDefinitions {

    InMemoryTimeTable timeTable = new InMemoryTimeTable();
    ItineraryService itineraryService = new ItineraryService(timeTable);

    @Given("the {line} train to {station} leaves {station} at {times}")
    public void theTrainLeavesAt(String line,
                                 String to,
                                 String from,
                                 List<LocalTime> departureTimes) {
        timeTable.scheduleService(line, departureTimes, from, to);
    }

    List<LocalTime> proposedDepartures;

    @When("Travis want to travel from {station} to {station} at {time}")
    public void travelBetween(String from, String to, LocalTime departureTime) {
        proposedDepartures = itineraryService.findNextDepartures(departureTime, from, to);
    }

    @Then("he should be told about the trains at: {times}")
    public void shouldBeToldAboutTheTrainsAt(List<LocalTime> expected) {
        assertThat(proposedDepartures).isEqualTo(expected);
    }

    @ParameterType(".*")
    public LocalTime time(String timeValue) {
        return LocalTime.parse(timeValue);
    }

    @ParameterType("T\\d+")
    public String line(String lineNumber) {
        return lineNumber;
    }

    @ParameterType(".*")
    public String station(String stationName) {
        return stationName;
    }

    @ParameterType(".*")
    public List<LocalTime> times(String timeValue) {
        return stream(timeValue.split(","))
                .map(String::trim)
                .map(LocalTime::parse)
                .collect(Collectors.toList());
    }
}
