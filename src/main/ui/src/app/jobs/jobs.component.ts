import {Component, OnInit} from '@angular/core';
import {JobService} from '../services/job.service';
import {Job} from '../models/models.job';
import {JobStats} from '../models/models.job-stats';
import {SafeUrl} from '@angular/platform-browser';
import {UserService} from '../services/user.service';
import {AlertService} from '../services/alert.service';

@Component({
  selector: 'app-jobs',
  templateUrl: './jobs.component.html',
  styleUrls: ['./jobs.component.css']
})
export class JobsComponent implements OnInit {

  constructor(private jobService: JobService,
              private userService: UserService,
              private alertService: AlertService) { }

  private pictureSrc: SafeUrl = null;
  private jobProfile: JobStats = new JobStats();
  private myJobs: Job[] = null;
  private jobs: Job[] = null;
  private skillBasedJobs: Job[] = null;

  private newJob: Job = new Job();

  private newSkill = '';

  ngOnInit() {


    this.userService.getProfilePicture().subscribe(
      response => {
        if (response.body !== null) {
          const base64data = response.body.image;
          const mimeType = response.body.type;
          this.pictureSrc = 'data:' + mimeType + ';base64,' + base64data;
        }
      },
      error => {
        console.log(error);
      }
    );

    this.jobService.getJobStats().subscribe(
      response => {
        this.jobProfile = response.body;
      },
      error => {
        console.log(error);
      }
    );

    this.jobService.getJobFeed().subscribe(
      response => {
        this.jobs = response.body;
      },
      error => {
        console.log(error);
      }
    );

    this.jobService.getSkillBasedFeed().subscribe(
      response => {
        this.skillBasedJobs = response.body;
      },
      error => {
        console.log(error);
      }
    );

    this.jobService.getMyJobs().subscribe(
      response => {
        this.myJobs = response.body;
      },
      error => {
        console.log(error);
      }
    );
  }

  newJobSubmit() {
    this.jobService.createJob(this.newJob).subscribe(
      response => {
        this.myJobs = [response.body, ...this.myJobs];
        this.newJob = new Job();
        this.alertService.success('Job posting submitted successfully');
      },
      error => {
        this.alertService.error('Unexpected error occured');
        console.log(error);
      }
    );
  }



  addSkill() {

    if (this.newSkill === '') {
      return;
    }
    if (!this.newJob.requiredSkills.includes(this.newSkill)) {
      this.newJob.requiredSkills.push(this.newSkill);
      this.newSkill = '';
    } else {
      this.alertService.error('Duplicate skill description');
    }
  }

  deleteSkill(skill: string) {
    this.newJob.requiredSkills = this.newJob.requiredSkills.filter(x => x !== skill);
  }

}
