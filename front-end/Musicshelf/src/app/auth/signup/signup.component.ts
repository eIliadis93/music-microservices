import { AuthorizationService } from './../../service/authorization.service';
import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/model/user';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss'],
})
export class SignupComponent implements OnInit {
  name: string = '';
  username: string = '';
  password: string = '';

  user: User = new User();

  constructor(private authService: AuthorizationService) {}

  ngOnInit(): void {
    this.username = '';
    this.password = '';
    this.name = '';
  }

  signup() {
    this.user.username = this.username;
    this.user.password = this.password;
    this.user.name = this.name;
    this.user.role = 'User';

    this.authService.signup(this.user).subscribe(result => {
      if(result === null){
        alert("Registration failed");
        this.ngOnInit();
      }
      else{
        console.log("Registration is successful.")
      }
    }, error =>{
      alert("Registration failed.");
      this.ngOnInit();
    })
  }
}
