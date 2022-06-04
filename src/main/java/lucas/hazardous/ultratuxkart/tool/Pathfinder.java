package lucas.hazardous.ultratuxkart.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record Pathfinder(byte[][] map) {

    public List<List<Integer>> findPathToTarget(List<Integer> mapTargetPoint, List<Integer> mapStartingPoint) {
        var result = findPathToTarget(mapTargetPoint, mapStartingPoint, new ArrayList<>());
        return result != null ? result : new ArrayList<>();
    }

    private List<List<Integer>> findPathToTarget(List<Integer> targetPosition, List<Integer> currentPosition, List<List<Integer>> visited) {
        if (!tilePartOfRoadAndNotVisited(currentPosition, visited)) return null;

        if (currentPosition.equals(targetPosition)) return List.of(currentPosition);

        visited.add(currentPosition);

        List<List<Integer>> result = null;
        int bestLength = -1;

        List<List<Integer>> options = Arrays.asList(
                Arrays.asList(currentPosition.get(0) + 1, currentPosition.get(1)),
                Arrays.asList(currentPosition.get(0) - 1, currentPosition.get(1)),
                Arrays.asList(currentPosition.get(0), currentPosition.get(1) + 1),
                Arrays.asList(currentPosition.get(0), currentPosition.get(1) - 1)
        );

        List<List<Integer>> tmpResult;

        for (List<Integer> option : options) {
            tmpResult = findPathToTarget(targetPosition, option, new ArrayList<>(visited));

            if (tmpResult == null) continue;
            tmpResult = Stream.concat(Stream.of(currentPosition), tmpResult.stream()).collect(Collectors.toList());

            if (bestLength == -1 || tmpResult.size() < bestLength) {
                bestLength = tmpResult.size();
                result = tmpResult;
            }
        }

        return result;
    }

    private boolean tilePartOfRoadAndNotVisited(List<Integer> option, List<List<Integer>> visited) {
        return (option.get(0) >= 0) && (option.get(0) < map.length) && (option.get(1) >= 0) && (option.get(1) < map[0].length)
                && map[option.get(0)][option.get(1)] != 0 && !visited.contains(option);
    }
}
