import { Component, OnInit, Input } from '@angular/core';
import { Observable } from 'rxjs';
import * as moment from 'moment';

@Component({
  selector: 'ng-timer',
  template: `{{ time }}`
})
export class NgTimerComponent implements OnInit {
  time: string;

  @Input()
  format: string = 'mm:ss';

  @Input()
  startTime: number;

  constructor() {
  }

  ngOnInit() {
    Observable
      .timer(0, 1000)
      .map(i => this.startTime - i)
      .take(this.startTime + 1)
      .subscribe(
        t => this.time = moment("2016-01-01").startOf('day')
          .seconds(t)
          .format(this.format)
      );
  }
}
