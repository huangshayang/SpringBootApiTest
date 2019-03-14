import {Component, OnInit} from '@angular/core';
import * as echarts from 'echarts';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {addDays, eachDay, endOfDay, endOfToday, format, startOfDay, subWeeks} from 'date-fns';
import {Router} from '@angular/router';
import {NzMessageService} from 'ng-zorro-antd';
import {FormBuilder, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-report-manage',
  templateUrl: './report-manage.component.html',
  styleUrls: ['./report-manage.component.css']
})
export class ReportManageComponent implements OnInit {
  status: number;
  data = [];
  timeForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private message: NzMessageService
  ) {
  }

  ngOnInit() {
    this.timeForm = this.fb.group({
      startTime: subWeeks(addDays(new Date(), 1), 1),
      endTime: endOfToday()
    });
    this.searchLog();
  }

  searchLog() {
    const formModel = this.timeForm.value;
    const body = {
      startTime: null,
      endTime: null
    };
    body.startTime = format(startOfDay(formModel.startTime), 'X');
    body.endTime = format(endOfDay(formModel.endTime), 'X');
    this.http.get('/log/search', {
      params: body, headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    }).subscribe(
      res => {
        this.status = res['status'];
        if (this.status === 1) {
          let t = 0;
          let f = 0;
          let e = 0;
          this.data = res['data'];
          this.data.map(item => item.checkBoolean).forEach(item => {
            if (item === 1) {
              t++;
            } else if (item === 0) {
              f++;
            } else {
              e++;
            }
          });
          this.line();
          this.pie(t, f, e);
          this.createSuccessMessage(res['message']);
        } else if (this.status === 10008) {
          this.router.navigate(['login']);
          this.createErrorMessage(res['message']);
        } else {
          this.createErrorMessage(res['message']);
        }
      }
    );
  }

  private line() {
    const myChart1 = echarts.init(document.getElementById('main1'));

    const option = {
      title: {
        text: '用例总数统计'
      },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: ['执行用例数']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: eachDay(this.timeForm.value.startTime, this.timeForm.value.endTime).map(dateArr => format(dateArr, 'M-D'))
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '执行用例数',
          type: 'line',
          color: ['#31b3f9'],
          stack: '总量',
          data: [120, 132, 101, 134, 90, 230, 210]
        }
      ]
    };

    myChart1.setOption(option);
  }

  private pie(t: number, f: number, e: number) {
    const myChart2 = echarts.init(document.getElementById('main2'));

    const option1 = {
      title: {
        text: '用例成功和失败统计',
        x: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{b} : {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: ['成功', '失败', '出错']
      },
      series: [
        {
          type: 'pie',
          color: ['#3dff40', '#f43722', '#fcf71b'],
          radius: ['50%', '70%'],
          center: ['50%', '60%'],
          data: [
            {value: t, name: '成功'},
            {value: e, name: '出错'},
            {value: f, name: '失败'}
          ],
          itemStyle: {
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };

    myChart2.setOption(option1);
  }


  private createSuccessMessage(success: string): void {
    this.message.success(success, {nzDuration: 3000});
  }

  private createErrorMessage(error: string): void {
    this.message.error(error, {nzDuration: 3000});
  }

}
