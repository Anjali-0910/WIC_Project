from django.urls import path
from performance_platform import views

urlpatterns = [
    path('', views.dashboard_view, name='dashboard'),
    path('api/health', views.health_check, name='health'),
    path('api/performance/upload', views.upload_report, name='upload'),
    path('api/performance/report', views.get_report, name='report'),
    path('api/performance/score', views.get_score, name='score'),
]
