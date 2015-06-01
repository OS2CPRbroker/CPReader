define(["modolus11"], function(modolus11) {
	return {
		validateQuery : function() {

			var queryfield = $('#query');
			var query = queryfield.val();

			var containsspecialcharacters = /\½|\§|\!|\"|\@|\#|\£|\¤|\$|\%|\&|\/|\{|\(|\[|\)|\]|\=|\}|\?|\+|\'|\`|\||\^|\~|\*|\_|\;|\:|\.|\+/;
			var containsnumbers = /[0-9]/;
			var containsletters = /[a-zA-ZæÆøØåÅ]/;
			var cprpattern = /[0-9]{6}-[0-9]/;

			//reset the color
			this.clearValidation(queryfield);

			//if the query has special characters
			if (containsspecialcharacters.test(query)) {
				queryfield.parent().addClass('has-warning');
				queryfield.attr('data-original-title', 'Bemærk');
				queryfield.attr('data-content',
						'Din søgning indeholder et eller flere specialtegn.')
				queryfield.popover('show');
			}

			//if the query contains numbers and letters
			else if (containsnumbers.test(query) & containsletters.test(query)) {
				queryfield.parent().addClass('has-warning');
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
					queryfield.parent().addClass('has-warning');
				}

				//if there is more than 5 numbers, but less than 10
				else if (query.length > 5 & query.length < 10) {
					// validate that the first 6 numbers is a valid date
					queryfield.parent().addClass('has-warning');
				}

				else if (query.length > 5 & query.length < 11
						& cprpattern.test(query)) {
					// validate that the first 6 numbers is a valid date    	  
					queryfield.parent().addClass('has-warning');
				}

				//if there is more than 10 numbers
				else if ((query.length > 10 & !cprpattern.test(query))
						| (query.length > 11 & cprpattern.test(query))) {
					queryfield.parent().addClass('has-error');
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
						queryfield.parent().addClass('has-success');
					} else if (!cprpattern.test(query) & modolus11.check(query)) {
						queryfield.parent().addClass('has-success');
					} else if (cprpattern.test(query)
							& modolus11.check(query.replace("-", ""))) {
						queryfield.parent().addClass('has-success');
					} else {
						queryfield.parent().addClass('has-error');
						queryfield.attr('data-original-title', 'Bemærk');
						queryfield.attr('data-content',
								'Dette er ikke et gyldigt CPR nummer.')
						queryfield.popover('show');
					}
				}
			}
			// everything else is considered valid, 
			else if (query.length > 0) {
				queryfield.parent().addClass('has-success');
			}
		},

		validateAddressQuery: function () {
			var addressQueryField = $('#addressQuery');
			var addressQuery = addressQueryField.val().trim();
			var online = ($("input[name=online]:checked").val() == "true");

			this.clearValidation(addressQueryField);

			if(online)
			{
				if(addressQuery.length == 0)
				{
					addressQueryField.parent().addClass('has-warning');
					addressQueryField.attr('data-original-title', 'Bemærk');
					addressQueryField.attr('data-content', 'You must provide an address when searching online');
					addressQueryField.popover('show');
				}
			}
		},

		validateQueries : function () {
			this.validateQuery();
			this.validateAddressQuery();
		},

		clearValidation: function (queryfield) {
			queryfield.parent().removeClass('has-success');
			queryfield.parent().removeClass('has-error');
			queryfield.parent().removeClass('has-warning');

			queryfield.popover('destroy');
		}
	}
});