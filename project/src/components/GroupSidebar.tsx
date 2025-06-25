import React from 'react';
import { Users, Settings, LogOut, Crown, Circle } from 'lucide-react';

const GroupSidebar: React.FC = () => {
  const activeMembers = [
    { id: 1, name: 'Sameep Kumar', status: 'online', isOwner: true },
    { id: 2, name: 'Rahul Sharma', status: 'online', isOwner: false },
    { id: 3, name: 'Priya Singh', status: 'busy', isOwner: false },
  ];

  const offlineMembers = [
    { id: 4, name: 'Amit Patel', status: 'offline', isOwner: false },
    { id: 5, name: 'Sarah Johnson', status: 'offline', isOwner: false },
  ];

  return (
    <div className="w-80 bg-white dark:bg-gray-900 border-r border-gray-200 dark:border-gray-700 h-full transition-colors duration-200">
      <div className="p-6">
        <div className="mb-6">
          <h2 className="text-xl font-bold text-gray-900 dark:text-white mb-2">Fun-Group</h2>
          <p className="text-sm text-gray-600 dark:text-gray-400">
            {activeMembers.length + offlineMembers.length} members
          </p>
        </div>

        <div className="space-y-6">
          <div>
            <div className="flex items-center space-x-2 mb-3">
              <Circle className="h-3 w-3 text-green-500 fill-current" />
              <h3 className="text-sm font-semibold text-gray-900 dark:text-white">
                Active Members ({activeMembers.length})
              </h3>
            </div>
            <div className="space-y-2">
              {activeMembers.map((member) => (
                <MemberItem key={member.id} member={member} />
              ))}
            </div>
          </div>

          <div>
            <div className="flex items-center space-x-2 mb-3">
              <Circle className="h-3 w-3 text-gray-400 fill-current" />
              <h3 className="text-sm font-semibold text-gray-900 dark:text-white">
                Offline Members ({offlineMembers.length})
              </h3>
            </div>
            <div className="space-y-2">
              {offlineMembers.map((member) => (
                <MemberItem key={member.id} member={member} />
              ))}
            </div>
          </div>
        </div>

        <div className="mt-8 pt-6 border-t border-gray-200 dark:border-gray-700">
          <div className="space-y-2">
            <button className="w-full flex items-center space-x-3 px-3 py-2 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg transition-colors duration-200">
              <Settings className="h-5 w-5" />
              <span>Group Settings</span>
            </button>
            <button className="w-full flex items-center space-x-3 px-3 py-2 text-red-600 dark:text-red-400 hover:bg-red-50 dark:hover:bg-red-900/20 rounded-lg transition-colors duration-200">
              <LogOut className="h-5 w-5" />
              <span>Leave Group</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

interface Member {
  id: number;
  name: string;
  status: 'online' | 'busy' | 'offline';
  isOwner: boolean;
}

const MemberItem: React.FC<{ member: Member }> = ({ member }) => {
  const getStatusColor = (status: string) => {
    switch (status) {
      case 'online':
        return 'bg-green-500';
      case 'busy':
        return 'bg-yellow-500';
      default:
        return 'bg-gray-400';
    }
  };

  return (
    <div className="flex items-center space-x-3 p-2 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-800 transition-colors duration-200">
      <div className="relative">
        <div className="w-8 h-8 bg-gradient-to-br from-blue-500 to-purple-600 rounded-full flex items-center justify-center">
          <span className="text-white text-sm font-medium">
            {member.name.split(' ').map(n => n[0]).join('')}
          </span>
        </div>
        <div className={`absolute -bottom-0.5 -right-0.5 w-3 h-3 ${getStatusColor(member.status)} rounded-full border-2 border-white dark:border-gray-900`} />
      </div>
      <div className="flex-1 min-w-0">
        <div className="flex items-center space-x-1">
          <p className="text-sm font-medium text-gray-900 dark:text-white truncate">
            {member.name}
          </p>
          {member.isOwner && (
            <Crown className="h-4 w-4 text-yellow-500" />
          )}
        </div>
        <p className="text-xs text-gray-500 dark:text-gray-400 capitalize">
          {member.status}
        </p>
      </div>
    </div>
  );
};

export default GroupSidebar;