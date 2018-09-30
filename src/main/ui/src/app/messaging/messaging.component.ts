import {Component, OnInit} from '@angular/core';
import {MessagingService} from '../services/messaging.service';
import {Conversation} from '../models/models.conversation';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-messaging',
  templateUrl: './messaging.component.html',
  styleUrls: ['./messaging.component.css']
})
export class MessagingComponent implements OnInit {

  private conversations: Conversation[] = null;
  private redirect: Boolean = null;

  private selectedConversation: Conversation = new Conversation();

  constructor(private route: ActivatedRoute,
              private messagingService: MessagingService) { }

  ngOnInit() {

    this.route.queryParams.subscribe(params => this.redirect = params.redirect);

    this.messagingService.getActiveConversations().subscribe(
      response => {
        this.conversations = response.body;
        if (this.conversations.length !== 0) {
          if (!this.redirect) {
            this.messagingService.registerSelection(this.conversations[0].conversationID);
          }
          this.selectionSetup();
        }
      },
      error => {
        console.log(error);
      }
    );

  }

  selectionSetup() {
    this.messagingService.getSelection().subscribe(
      selection => {
        this.selectedConversation = this.conversations
                                        .find((x) => x.conversationID === selection);
      },
      error => {
        console.log(error);
      }
    );
  }

  onConversationSelect(conversation: Conversation) {
    this.messagingService.registerSelection(conversation.conversationID);
  }


}
