import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { ThesisPortalSharedModule, UserRouteAccessService } from './shared';
import { ThesisPortalHomeModule } from './home/home.module';
import { ThesisPortalAdminModule } from './admin/admin.module';
import { ThesisPortalAccountModule } from './account/account.module';
import { ThesisPortalEntityModule } from './entities/entity.module';
import { ThesisModule } from './thesis/thesis.module';
import { ReportModule } from './report/report.module';

import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

import {
    JhiMainComponent,
    LayoutRoutingModule,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ErrorComponent
} from './layouts';

@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        ThesisPortalSharedModule,
        ThesisPortalHomeModule,
        ThesisPortalAdminModule,
        ThesisPortalAccountModule,
        ThesisPortalEntityModule,
        ThesisModule,
        ReportModule
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        FooterComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class ThesisPortalAppModule {}
