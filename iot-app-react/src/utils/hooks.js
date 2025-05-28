import { useMemo } from 'react';

export const useSelectedData = (allData, selectedIds) => {
  return useMemo(() => {
    return allData.filter(item => selectedIds.includes(item.id));
  }, [allData, selectedIds]);
};