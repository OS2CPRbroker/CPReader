define(["modolus11"], function(modolus11) {
	return {
		validateQuery : function(queryfield, query, querygroup) {

			var containsspecialcharacters = /\½|\§|\!|\"|\@|\#|\£|\¤|\$|\%|\&|\/|\{|\(|\[|\)|\]|\=|\}|\?|\+|\'|\`|\||\^|\~|\*|\_|\;|\:|\.|\+/;
			var containsnumbers = /[0-9]/;
			var containsletters = /[a-zA-Z]/;
			var cprpattern = /[0-9]{6}-[0-9]/;

			//reset the color
			querygroup.removeClass('has-success');
			querygroup.removeClass('has-error');
			querygroup.removeClass('has-warning');

			queryfield.popover('destroy');

			//if the query has special characters
			if (containsspecialcharacters.test(query)) {
				querygroup.addClass('has-warning');
				queryfield.attr('data-original-title', 'Bemærk');
				queryfield.attr('data-content',
						'Din søgning indeholder et eller flere specialtegn.')
				queryfield.popover('show');
			}

			//if the query contains numbers and letters
			else if (containsnumbers.test(query) & containsletters.test(query)) {
				querygroup.addClass('has-warning');
				queryfield.attr('data-original-title', 'Bemærk');
				queryfield
						.attr('data-content',
								'Din søgning indeholder en kombination af tal og bogstaver')
				queryfield.popover('show');

			}

			//if the query has only numbers
			else if (containsnumbers.test(query) & !containsletters.test(query)
					& !containsspecialcharacters.test(query)) {
				//if there is less than 6 numbers
				if (query.length < 6) {
					querygroup.addClass('has-warning');
				}

				//if there is more than 5 numbers, but less than 10
				else if (query.length > 5 & query.length < 10) {
					// validate that the first 6 numbers is a valid date
					querygroup.addClass('has-warning');
				}

				else if (query.length > 5 & query.length < 11
						& cprpattern.test(query)) {
					// validate that the first 6 numbers is a valid date    	  
					querygroup.addClass('has-warning');
				}

				//if there is more than 10 numbers
				else if ((query.length > 10 & !cprpattern.test(query))
						| (query.length > 11 & cprpattern.test(query))) {
					querygroup.addClass('has-error');
					queryfield.attr('data-original-title', 'Bemærk');
					queryfield
							.attr('data-content',
									'Din søgning indeholder over 10 tal. Et CPR nummer indeholder kun 10.')
					queryfield.popover('show');

				}

				//if there is 10 numbers
				else if ((query.length == 10 & !cprpattern.test(query))
						| (query.length == 11 & cprpattern.test(query))) {
					// is the date 010165 or 010166
					if (query.substring(0, 6) == '010165'
							| query.substring(0, 6) == '010166') {
						querygroup.addClass('has-success');
					} else if (!cprpattern.test(query) & modolus11.check(query)) {
						querygroup.addClass('has-success');
					} else if (cprpattern.test(query)
							& modolus11.check(query.replace("-", ""))) {
						querygroup.addClass('has-success');
					} else {
						querygroup.addClass('has-error');
						queryfield.attr('data-original-title', 'Bemærk');
						queryfield.attr('data-content',
								'Dette er ikke et gyldigt CPR nummer.')
						queryfield.popover('show');
					}
				}
			}
			// everything else is considered valid, 
			else if (query.length > 0) {
				querygroup.addClass('has-success');
			}
		}
	}
});