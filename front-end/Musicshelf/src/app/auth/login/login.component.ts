import { AuthorizationService } from './../../service/authorization.service';
import { User } from './../../model/user';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  username : string = '';
  password : string = '';
  role : string = '';

  user : User = new User();

  roles : string[];

  constructor(private authService: AuthorizationService) {
    this.roles = [
      'Admin',
      'User'
    ]
  }

  ngOnInit(): void {
  }

  login() {
    this.user.username = this.username;
    this.user.password = this.password;
    this.user.role = this.role;
    }
}
