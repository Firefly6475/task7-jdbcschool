package ua.com.foxminded.jdbcschool.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import ua.com.foxminded.jdbcschool.domain.Group;

public class GroupGeneratorImpl implements GroupGenerator {
    private static final String LETTER_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGIT_ALPHABET = "0123456789";
    private static final String HYPHEN_SYMBOL = "-";
    
    private final Random random;
    
    public GroupGeneratorImpl(Random random) {
        this.random = random;
    }
    
    @Override
    public List<Group> generateGroups(int amountOfGroups) {
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < amountOfGroups; i++) {
            String groupName = generateRandomGroupName();
            groups.add(Group.builder()
                    .withId(UUID.randomUUID().toString())
                    .withName(groupName)
                    .build());
        }
        return groups;
    }
    
    private String generateRandomGroupName() {
        StringBuilder nameBuilder = new StringBuilder();
        return nameBuilder.append(getRandomCharFromAlphabet(LETTER_ALPHABET))
                .append(getRandomCharFromAlphabet(LETTER_ALPHABET))
                .append(HYPHEN_SYMBOL)
                .append(getRandomCharFromAlphabet(DIGIT_ALPHABET))
                .append(getRandomCharFromAlphabet(DIGIT_ALPHABET))
                .toString();
    }
    
    private char getRandomCharFromAlphabet(String alphabet) {
        return alphabet.charAt(random.nextInt(alphabet.length()));
    }
}
