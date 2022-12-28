import { useSelector } from 'react-redux';

import {getCurrentExpandSidebar} from '../../app/reducer/expandSidebar/expandSidebarSlice';

import { useWindowResize } from './useWindowResize';

export const useGetRightWidth = (): number => {
  const isExpand: boolean = useSelector(getCurrentExpandSidebar);
  const { width } = useWindowResize();
  const screenWidth = width ?? 0;
  const sideBarWidth = screenWidth < 1100 ? 0 : isExpand ? 220 : 70;
  const rightWidth = screenWidth - sideBarWidth;

  return rightWidth;
};
