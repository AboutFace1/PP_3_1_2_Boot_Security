$(function () {
    $('.logoutButton').append(`<div class="logoutButton" style="margin-top: 5px">
  <form method="POST" action="/logout">
    <button type="submit" class="btn btn-danger btn-block">Log out</button>
  </form>
</div>`)
});