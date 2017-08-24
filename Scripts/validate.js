define(["modolus11"], function(modolus11) {
	return {
		validateQuery : function() {

			var queryfield = $('#query');
			var query = queryfield.val();

			var containsspecialcharacters = /\½|\§|\!|\"|\@|\#|\£|\¤|\$|\%|\&|\/|\{|\(|\[|\)|\]|\=|\}|\?|\+|\'|\`|\||\^|\~|\*|\_|\;|\:|\.|\+/;
			var containsnumbers = /[0-9]/;
			var containsletters = /[a-zA-ZæÆøØåÅ]/;
			var cprpattern = /[0-9]{6}-[0-9]/;

			//reset the colors
			this.clearValidation(queryfield);
			var pnrMode = false;

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
				// Disable address & online inputs
				pnrMode = true;

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
				else if ((query.length === 10 & !cprpattern.test(query))
						| (query.length === 11 & cprpattern.test(query))) {
					// is the date 010165 or 010166
					if (query.substring(0, 6) === '010165'
							| query.substring(0, 6) === '010166') {
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

			// Finally, set PNR mode
			this.setPnrMode(pnrMode);
		},

		validateAddressQuery: function (doneCallBack) {

			var addressQueryField = $('#addressQuery');
			var addressQuery = addressQueryField.val().trim();
			var validateDawa = addressQueryField.attr('validateDawa');
			this.clearValidation(addressQueryField);

			if(!doneCallBack){
				doneCallBack = function(){};
			}

			// Prohibit name only search
			var online = ($("input[name=online]:checked").val() === "true");
			if (online && !addressQueryField.attr('disabled')) {
				if (addressQuery.length === 0) {
					addressQueryField.parent().addClass('has-warning');
					addressQueryField.attr('data-original-title', 'Bemærk');
					addressQueryField.attr('data-content', addressQueryField.attr('addressRequiredText'));
					addressQueryField.popover('show');
				}
			}

			// Invalid addresses
			if(validateDawa === "True")
			{
				$.ajax({
					url:		document.location.protocol + '//dawa.aws.dk/adresser/autocomplete',
					type: 		"GET",
					dataType: 	"jsonp",
					data: 		{q: addressQuery, maxantal: 1},
					//async: 		false,
					success: 	function (data) {
						if(data.length === 0){
							addressQueryField.parent().addClass('has-warning');
							addressQueryField.attr('data-original-title', 'Bemærk');
							addressQueryField.attr('data-content', addressQueryField.attr('invalidAddressText'));
							addressQueryField.popover('show');
						}
						doneCallBack();
					},
					error: 		function (textStatus) {
						alert("Der kunne ikke hentes data fra AWS API'et. Serveren returnerede følgende:\n'" + textStatus + "'\n\nAdresseforslag virker derfor ikke pt.");
					}
				});
			}
			else
			{
				doneCallBack();
			}
		},

		validateQueries : function (doneCallBack) {
			this.validateQuery();
			this.validateAddressQuery(doneCallBack);
		},

		setPnrMode: function (isPnrMode){
			$('#addressQuery').attr('disabled', isPnrMode);
			$("input[name=online]").attr('disabled', isPnrMode);
			if(isPnrMode){
				this.clearValidation($('#addressQuery'));
			} else {
				this.validateAddressQuery();
			}
		},

		clearValidation: function (queryfield) {
			queryfield.parent().removeClass('has-success');
			queryfield.parent().removeClass('has-error');
			queryfield.parent().removeClass('has-warning');

			queryfield.popover('destroy');
        },

        resolveAmbiguousAddresses: function (doneCallBack) {
            var addressQueryField = $('#addressQuery');
            var addressQuery = addressQueryField.val().trim();

            if (!doneCallBack) {
                doneCallBack = function (){};
            }

            $.ajax({
                url: document.location.protocol + '//dawa.aws.dk/adresser/autocomplete',
                type: "GET",
                dataType: "jsonp",
                data: { q: addressQuery, maxantal: 1 },
                async: true,
                success: function (data) {
                    if (data.length > 1) {
                        addressQueryField.val(data[0].tekst);
                    }
                    doneCallBack();
                },
                error: function (textStatus) {
                    alert("Der kunne ikke hentes data fra AWS API'et. Serveren returnerede følgende:\n'" + textStatus + "'\n\nAdresseforslag virker derfor ikke pt.");
                    doneCallBack();
                }
            });
        }
	}
});