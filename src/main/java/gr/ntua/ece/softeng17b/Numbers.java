package gr.ntua.ece.softeng17b;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Numbers {

	private List<Long> numbers;

	public Numbers(List<Long> numbers) {
		this.numbers = numbers;
	}

	public static Numbers parse(String string) {
		String[] array = string.split(",");
		List<Long> numbers = Arrays.asList(array)
		             			   .stream()
		             			   .map(s -> Long.valueOf(s.trim()))
		             			   .collect(Collectors.toList());
		return new Numbers(numbers);
	}

	public Long sum() {
		return numbers.stream().reduce(0L, (x,y) -> x + y);
	}

	public int count() {
		return numbers.size();
	} 
}