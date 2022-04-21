
async function fetchAndFill() {
     await getUsersTable();
     await addNewUserListener();
     await modalListener();
     await deleteButtonListener();
     await getAuthUserData();

 }

 const url = 'http://localhost:8080/api/users';

function clearUsersTable() {
    let bodyTable = $('.tab-pane.active tbody');
    bodyTable.html('');
}

async function getAuthUserData() {
    await fetch('http://localhost:8080/api/users/authenticated-user')
        .then(resp => resp.json())
        .then(user => {
                $('.spanAuthUserName').text(`${user.email}`);
                $('.spanAuthUserRole').text(`${user.roles[0].name === 'ADMIN' ? 'ADMIN' : 'USER'}`);
        })
}

async function getUsersTable() {
    console.log('got users table')
     let bodyTable = $('.tab-pane.active tbody');

     fetch(url)
         .then(resp => resp.json())
         .then(users => {
             users.forEach(user => {
                 console.log(`${user.roles.map(x=>`${x.name}`)}`);
                 let tableRow = `$(
                    <tr>
                     <td>${user.id}</td>
                     <td>${user.name}</td>
                     <td>${user.age}</td>
                     <td>${user.roles[0].name === 'ADMIN' ? 'ADMIN' : 'USER'}</td>
                     <td>${user.email}</td>
            
                     <td>
                         <button type="button" id="editUserButton" class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#my_modal"
                         data-user-id="${user.id}" data-action="edit">Edit
                         </button>
                     </td>
            
                     <td>
                         <button type="button" id="deleteUserButton" class="btn btn-close"
                          data-user-id="${user.id}" data-action="delete"> <!-- data-user-id changes to userId when calling dataset.userId -->
                         </button>
                     </td>
                    </tr>
                )`;

                 bodyTable.append(tableRow);
             })
         })
 }

 async function deleteButtonListener() {
     $('.table-striped').on('click', '#deleteUserButton', async function (e) {

         const response = await userFetchService.deleteUser(e.target.dataset.userId);
         console.log(e.target.dataset.userId)
         if (response.ok) {
             await clearUsersTable();
             await getUsersTable();
         }
     })
 }

 async function modalListener() {
     $('#my_modal').on("show.bs.modal", async function(e) {
         console.log('works modal')

         let editButton = $(e.relatedTarget);
         let userid = editButton.data('user-id');
         let action = editButton.data('action');

         if (action === 'edit') {
             await editUser($(e.target), userid); // view
         }
     }).on("hidden.bs.modal", (e) => {
         console.log('works hidden modal')

         let thisModal = $(e.target);

         thisModal.find('.modal-title').html('');
         thisModal.find('.modal-body').html('');
         thisModal.find('.modal-footer').html('');
     })
 }

 async function editUser(modal, id) {
     let restUser = await userFetchService.findUser(id);
     let user = restUser.json();

     $('.modal-title').html("Edit");

     let closeButton = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>`;
     let editButton = `<button  class="btn btn-primary" id="editButton">Edit</button>`;

     $('.modal-footer').append(closeButton, editButton);

     user.then(user => {
         let bodyForm = `<form class="form-group" id="editUser">
            <div class="mb-3">
                <label for="input-id" class="col-form-label">Id:</label>
                <input type="text" name="id" class="form-control" id="input-id" value="${user.id}" readonly>
            </div>
            <div class="mb-3">
                <label for="input-name" class="col-form-label">Name:</label>
                <input type="text" name="name" class="form-control" id="input-name" value="${user.name}">
            </div>
            <div class="mb-3">
                <label for="input-age" class="col-form-label">Age:</label>
                <input type="range" min="1" max="127" oninput="this.form.amountInput.value=this.value"
                       name="age" class="form-range" id="input-age" value="${user.age}"> <!-- <output id="ageOutput"></output> -->
                <label>
                    <input type="number" name="amountInput" min="1" max="127" value="${user.age}" oninput="this.form.age.value=this.value" />
                </label>
            </div>
            <label>Role:</label><br>
            <div class="form-check form-check-inline">
                <label for="input-USER" class="form-check-label">USER</label>
                <input type="checkbox" class="form-check-input" name="role" value="USER" id="input-USER" onclick="onlyOne(this)">
            </div>
            <div class="form-check form-check-inline">
                <label for="input-ADMIN" class="form-check-label">ADMIN</label>
                <input type="checkbox" class="form-check-input" name="role" value="ADMIN" id="input-ADMIN" onclick="onlyOne(this)">
            </div>
            
            <div class="mb-3">
                <label for="input-email" class="col-form-label">Email:</label>
                <input type="text" name="email" class="form-control" id="input-email" value="${user.email}">
            </div>

        </form>`;

         $('.modal-body').append(bodyForm);

        // Mark current role
         if (user.roles[0].name === 'ADMIN') {
            document.querySelector('#input-ADMIN').setAttribute('checked', 'checked')
             document.querySelector('#input-USER').removeAttribute('checked')
         } else {
            document.querySelector("#input-USER").setAttribute('checked','checked')
            document.querySelector("#input-ADMIN").removeAttribute('checked')
         }
     })

     $("#editButton").on('click', async (e) => {
         e.preventDefault(); // or 'button type = "button"'

         let id = modal.find('#input-id').val().trim();
         let name = modal.find('#input-name').val().trim();
         let email = modal.find('#input-email').val().trim();
         let age = modal.find('#input-age').val().trim();
         let role = $('.form-check-input:checked').val();
         let roleId = role === 'ADMIN' ? 10 : 20;
         console.log('Role is:'+role)
         console.log('RoleId is:'+roleId)

         let data = {
             id: id,
             name: name,
             email: email,
             age: age,
             roles: [{id:roleId}, {name:role}]
         }

         const response = await userFetchService.updateUser(data, id);

         if (response.ok) {
             clearUsersTable();
             await getUsersTable();
             modal.modal('hide');
             console.log("user have been updated");
         } else {
             let body = await response.json();
             let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="errorMessage">
                        ${body.info}
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>`;
             modal.find('.modal-body').prepend(alert);
         }
     })
 }

 async function addNewUserListener() {
    console.log("add new listener")
     $('#addNewUserButton').on('click', async (e) =>  {
         e.preventDefault();

         let name = $('#newName').val();
         let age = $('#newAge').val();
         let role = $('.form-check-input:checked').val();
         let roleId = role === 'ADMIN' ? 10 : 20;

         let email = $('#newEmail').val();
         let password = $('#newPassword').val();
         let data = {
             name: name,
             age: age,
             email: email,
             password: password,
             roles: [{id:roleId}, {name:role}]
         }
         const response = await userFetchService.addNewUser(data);
         if (response.ok) {
             document.getElementById('mainTab').click(); // Switch to Users Table tab after adding new user
             clearNewUserForm();
             clearUsersTable();
             await getUsersTable();
         }
     })
 }

function clearNewUserForm() {
    $('.mb-3 #newName').val('');
    $('.mb-3 #newAge').val('');
    $('.mb-3 #newEmail').val('');
    $('.mb-3 #newPassword').val('');
    $('.mb-3 #newCheckbox').prop('checked', false);
    $('.form-check .form-check-input:checked').prop('checked', false);
}



const userFetchService = {
     headers: {
         'Accept': 'application/json',
         'Content-type': 'application/json'
     },

     updateUser: async (user, id) => await fetch(`api/users/${id}`,
         {method: 'PATCH', headers: userFetchService.headers, body: JSON.stringify(user)}),

     findUser: async (id) => await fetch(`api/users/${id}`),
     deleteUser: async (id) => await fetch(`api/users/${id}`, {method: 'DELETE', headers: userFetchService.headers}),
     addNewUser: async (user) => await fetch('api/users',
         {method: 'POST', headers: userFetchService.headers, body: JSON.stringify(user)})
 }


