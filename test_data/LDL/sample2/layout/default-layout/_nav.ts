interface NavAttributes {
  [propName: string]: any;
}
interface NavWrapper {
  attributes: NavAttributes;
  element: string;
}
interface NavBadge {
  text: string;
  variant: string;
}
interface NavLabel {
  class?: string;
  variant: string;
}

export interface NavData {
  name?: string;
  url?: string;
  icon?: string;
  badge?: NavBadge;
  title?: boolean;
  children?: NavData[];
  variant?: string;
  attributes?: NavAttributes;
  divider?: boolean;
  class?: string;
  label?: NavLabel;
  wrapper?: NavWrapper;
}

export let navItems: NavData[] = [
  {
    name: 'Dashboard',
    url: '/dashboard',
    icon: 'icon-speedometer',
  },
  {
    title: true,
    name: 'Web App'
  },

  {
    name: 'PatientMonitoring',
    icon: 'icon-user-following',
    children: [
      {
        name: 'main',
        url: '/patient-monitoring/main',
        icon: 'icon-mouse'
      },
      {
        name: 'setTimePage',
        url: '/patient-monitoring/set-time-page',
        icon: 'icon-star'
      },
      {
        name: 'patientPage',
        url: '/patient-monitoring/patient-page',
        icon: 'icon-star'
      },
      {
        name: 'endPage',
        url: '/patient-monitoring/end-page',
        icon: 'icon-star'
      }
    ]
  },
  {
    title: true,
    name: 'Other'
  },
  {
    name: '(Under Construction)',
    url: '/under-construction',
    icon: 'icon-wrench'
  },
];

export function newNav(){
  return [{
    name: '(Under Construction)',
    url: '/under-construction',
    icon: 'icon-wrench'
  }];
}
