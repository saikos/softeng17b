package gr.ntua.ece.softeng17b

import spock.lang.Specification
import spock.lang.Unroll

class NumbersSpec extends Specification {

	@Unroll
	def "Given the input '#str' the sum is #sum, the count is #count"() {
		
		expect:
		Numbers n = Numbers.parse(str)
		assert n.sum() == sum
		assert n.count() == count

		where:
		str     | sum | count
		"1"     | 1   | 1
		"1, 2"  | 3   | 2
		"3,4,1" | 8   | 3
		"-1,0,1"| 0   | 3
		"α,b,12"| 12  | 1
	}
}