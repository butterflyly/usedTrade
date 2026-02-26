   const pwRegex = /^(?=.*[a-zA-Z])(?=.*\d).{12,}$/;

   const form = document.getElementById("pwChangeForm");
   const newPw1 = document.getElementById("newPassword1");
   const newPw2 = document.getElementById("newPassword2");

   const newPwError = document.getElementById("newPwError");
   const matchError = document.getElementById("newPwMatchError");
   const prePwError = document.getElementById("prePwError");

   function validateNewPassword() {
       newPwError.innerText = "";
       matchError.innerText = "";

       if (!pwRegex.test(newPw1.value)) {
           newPwError.innerText = "영문+숫자 포함 12자 이상이어야 합니다.";
           return false;
       }

       if (newPw1.value !== newPw2.value) {
           matchError.innerText = "비밀번호가 일치하지 않습니다.";
           return false;
       }
       return true;
   }

   newPw1.addEventListener("input", validateNewPassword);
   newPw2.addEventListener("input", validateNewPassword);

   form.addEventListener("submit", async e => {
       e.preventDefault();

       if (!validateNewPassword()) return;

       prePwError.innerText = "";

       const data = {
           prePassword: form.prePassword.value,
           newPassword1: newPw1.value
       };

       const res = await fetch("/api/users/pwchange", {
           method: "POST",
           headers: { "Content-Type": "application/json" },
           body: JSON.stringify(data)
       });

       if (res.ok) {
           alert("비밀번호가 변경되었습니다.");
           location.href = "/users/mypage";
       } else {
           const msg = await res.text();
           prePwError.innerText = msg;
       }
   });