
/**
 * Mandatory validation handler, tests that the value of the given field has a certain length (min = 0 and max = 4000)
 *
 * @method maxTextLength
 * @param field {object} The element representing the field the validation is for
 * @param args {object} Not used
 * @param event {object} The event that caused this handler to be called, maybe null
 * @param form {object} The forms runtime class instance the field is being managed by
 * @param silent {boolean} Determines whether the user should be informed upon failure
 * @param message {string} Message to display when validation fails, maybe null
 * @static
 */
Alfresco.forms.validation.maxTextLength = function maxTextLength(field, args, event, form, silent, message)
{
   var min = 0,
      max = 4000,
      length = YAHOO.lang.trim(field.value).length;

   if (length > max)
   {
      this.message = YAHOO.lang.substitute(Alfresco.messages.global["message.max.text.length.validation"],
      {
         max: max
      });
   }

   return (length > min && length <= max) ? true : false;
};