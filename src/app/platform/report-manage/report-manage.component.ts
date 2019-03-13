import {Component, OnInit} from '@angular/core';
import * as echarts from 'echarts';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Router} from '@angular/router';
import {NzMessageService} from 'ng-zorro-antd';

@Component({
  selector: 'app-report-manage',
  templateUrl: './report-manage.component.html',
  styleUrls: ['./report-manage.component.css']
})
export class ReportManageComponent implements OnInit {
  status: number;
  data = [];

  constructor(
    private http: HttpClient,
    private router: Router,
    private message: NzMessageService
  ) {
  }

  ngOnInit() {
    const myChart1 = echarts.init(document.getElementById('main1'));
    const myChart2 = echarts.init(document.getElementById('main2'));

    const option = {
      title: {
        text: '用例统计',
        subtext: '成功和失败',
        x: 'center'
      },
      color: ['#3398DB'],
      tooltip: {
        trigger: 'axis',
        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
          type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: [
        {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
          axisTick: {
            alignWithLabel: true
          }
        }
      ],
      yAxis: [
        {
          type: 'value'
        }
      ],
      series: [
        {
          type: 'bar',
          barWidth: '60%',
          data: [10, 52, 200, 334, 390, 330, 220]
        }
      ]
    };

    const option1 = {
      title: {
        text: '用例统计',
        subtext: '成功和失败',
        x: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left',
        data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
      },
      series: [
        {
          type: 'pie',
          radius: '55%',
          center: ['50%', '60%'],
          data: [
            {value: 335, name: '直接访问'},
            {value: 310, name: '邮件营销'},
            {value: 234, name: '联盟广告'},
            {value: 135, name: '视频广告'},
            {value: 1548, name: '搜索引擎'}
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

    myChart1.setOption(option);
    myChart2.setOption(option1);
  }

  get(startTime: number, endTime: number) {
    const params = new HttpParams()
      .append('startTime', `${startTime}`)
      .append('startTime', `${endTime}`);
    this.http.get('/log/search', {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      }),
      params
    }).subscribe(
      res => {
        this.status = res['status'];
        if (this.status === 1) {
          this.data = res['data'];
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

  private createSuccessMessage(success: string): void {
    this.message.success(success, {nzDuration: 3000});
  }

  private createErrorMessage(error: string): void {
    this.message.error(error, {nzDuration: 3000});
  }

}
