package lucas.hazardous.ultratuxkart.tool;

import java.util.ArrayList;
import java.util.List;

public class Pathfinder {
    private byte[][] map;

    private int bestResultLength;
    private List<List<List<Integer>>> results = new ArrayList<>();

    public Pathfinder(byte[][] map) {
        this.map = map;
        this.bestResultLength = map.length*map[0].length;
    }

    public List<List<Integer>> findPathToTarget(List<Integer> mapTargetPoint, List<Integer> mapStartingPoint) {
        findPathToTarget(mapTargetPoint, mapStartingPoint, new ArrayList<>());

        return results.size() > 0 ? results.get(results.size() - 1) : new ArrayList<>();
    }

    private List<List<Integer>> findPathToTarget(List<Integer> targetPosition, List<Integer> currentPosition, List<List<Integer>> solution) {
        //base case - found point is target point
        if (currentPosition.equals(targetPosition)) {
            solution.add(currentPosition);
            return solution;
        } else {
            List<List<Integer>> result = new ArrayList<>(solution);
            result.add(currentPosition);

            //all locations that are possible to reach from current point
            int[][] options = new int[][]{
                    {currentPosition.get(0) + 1, currentPosition.get(1)},
                    {currentPosition.get(0) - 1, currentPosition.get(1)},
                    {currentPosition.get(0), currentPosition.get(1) - 1},
                    {currentPosition.get(0), currentPosition.get(1) + 1}
            };

            //store temporary result from recursion
            List<List<Integer>> tmpResult;

            //placeholder for int[] from options
            List<Integer> currentOptionButList;

            //check every possible location
            for (int[] option : options) {
                currentOptionButList = new ArrayList<>();
                currentOptionButList.add(option[0]);
                currentOptionButList.add(option[1]);

                //check if location exists, if it's a part of path and if it was not visited
                if ((option[0] >= 0) && (option[0] < map.length) && (option[1] >= 0) && (option[1] < map[0].length)
                        && map[option[0]][option[1]] != 0 && !result.contains(currentOptionButList)) {

                    tmpResult = findPathToTarget(targetPosition, currentOptionButList, new ArrayList<>(result));

                    //check if found result is better
                    if (tmpResult.size() < bestResultLength && tmpResult.size() > 1 && tmpResult.get(tmpResult.size()-1).equals(targetPosition)) {
                        bestResultLength = tmpResult.size();
                        result = new ArrayList<>(tmpResult);
                        results.add(result);
                    }
                }
            }
            return result;
        }
    }
}
