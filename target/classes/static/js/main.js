 function popUpWithData() {
     $('#my_modal').on('show.bs.modal', function(e) {
         //get data-id attribute of the clicked element
         var userName = $(e.relatedTarget).data('user-name');
         var userAge = $(e.relatedTarget).data('user-age');
         var userRole = $(e.relatedTarget).data('user-role');
         var userEmail = $(e.relatedTarget).data('user-email');
         var userId = $(e.relatedTarget).data('user-id');

         $(e.currentTarget).find('input[name="name"]').val(userName);
         $(e.currentTarget).find('input[name="age"]').val(userAge);
         $(e.currentTarget).find('input[name="check"]').val(userRole);
         $(e.currentTarget).find('input[name="email"]').val(userEmail);
         $(e.currentTarget).find('form[name="idForm"]').attr('action', '/admin/' + userId);
         $(e.currentTarget).find('input[name="id"]').val(userId);
         $(e.currentTarget).find('input[name="amountInput"]').val(userAge);

         if (userRole === 'ADMIN') {
             document.getElementById("input-ADMIN").setAttribute("checked","checked")
             document.getElementById("input-USER").removeAttribute("checked")
         } else {
             document.getElementById("input-USER").setAttribute("checked","checked")
             document.getElementById("input-ADMIN").removeAttribute("checked")
         }

     });
 }



