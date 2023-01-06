import { Component, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.scss']
})
export class WelcomeComponent implements OnInit {

  name: string = "";
  userClaims: any = this.oauthService.getIdentityClaims();

  constructor(private oauthService: OAuthService) { }

  ngOnInit(): void {
  }

  get token(){
    let claims: any = this.oauthService.getIdentityClaims();
    return claims ? claims:null;
  }
}
