define(function () {
    return {
        check: function (numbers) {
            var num = 4 * numbers.charAt(0) +
                3 * numbers.charAt(1) +
                2 * numbers.charAt(2) +
                7 * numbers.charAt(3) +
                6 * numbers.charAt(4) +
                5 * numbers.charAt(5) +
                4 * numbers.charAt(6) +
                3 * numbers.charAt(7) +
                2 * numbers.charAt(8) +
                1 * numbers.charAt(9);

            if (
                num % 11 == 0
                | numbers.substring(0, 6) == '010165'
                | numbers.substring(0, 6) == '010166')
                return true;

            return false;
        }
    }
});